package com.bailbots.fb.quartersbot.service.impl;

import com.bailbots.fb.quartersbot.dao.HouseImageData;
import com.bailbots.fb.quartersbot.parser.buttonKeyboard.ButtonKeyboard;
import com.bailbots.fb.quartersbot.parser.buttonKeyboard.ButtonLink;
import com.bailbots.fb.quartersbot.parser.genericKeyboard.*;
import com.bailbots.fb.quartersbot.parser.persistentKeyboard.PersistentCategory;
import com.bailbots.fb.quartersbot.parser.persistentKeyboard.PersistentKeyboard;
import com.bailbots.fb.quartersbot.parser.persistentKeyboard.PersistentLink;
import com.bailbots.fb.quartersbot.parser.quickreplyKeyboard.QuickReplyButton;
import com.bailbots.fb.quartersbot.parser.quickreplyKeyboard.QuickReplyKeyboard;
import com.bailbots.fb.quartersbot.repository.KeyboardObjectRepository;
import com.bailbots.fb.quartersbot.service.KeyboardLoaderService;
import com.bailbots.fb.quartersbot.utils.callback.CallbackBuilder;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.messenger4j.common.SupportedLocale;
import com.github.messenger4j.messengerprofile.persistentmenu.LocalizedPersistentMenu;
import com.github.messenger4j.messengerprofile.persistentmenu.PersistentMenu;
import com.github.messenger4j.messengerprofile.persistentmenu.action.CallToAction;
import com.github.messenger4j.messengerprofile.persistentmenu.action.NestedCallToAction;
import com.github.messenger4j.messengerprofile.persistentmenu.action.PostbackCallToAction;
import com.github.messenger4j.send.message.TemplateMessage;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import com.github.messenger4j.send.message.template.GenericTemplate;
import com.github.messenger4j.send.message.template.button.Button;
import com.github.messenger4j.send.message.template.button.PostbackButton;
import com.github.messenger4j.send.message.template.common.Element;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class KeyboardLoaderServiceImpl implements KeyboardLoaderService {
    private static final String PERSISTENT_KEYBOARD_PATH = "src/main/resources/keyboard/persistent/";
    private static final String BUTTON_KEYBOARD_PATH = "src/main/resources/keyboard/button/";
    private static final String GENERIC_KEYBOARD_PATH = "src/main/resources/keyboard/generic/";
    private static final String QUICK_REPLY_KEYBOARD_PATH = "src/main/resources/keyboard/quickreply/";
    private static final String KEYBOARD_DAO_PATH = "com.bailbots.fb.quartersbot.dao.";
    private static final String KEYBOARD_REPOSITORY_PATH = "com.bailbots.fb.quartersbot.repository.";

    private final KeyboardObjectRepository keyboardObjectRepository;
    private final ApplicationContext applicationContext;

    public KeyboardLoaderServiceImpl(KeyboardObjectRepository keyboardObjectRepository, ApplicationContext applicationContext) {
        this.keyboardObjectRepository = keyboardObjectRepository;
        this.applicationContext = applicationContext;
    }

    @Override
    @SneakyThrows
    public PersistentMenu getPersistentMenuFromXML(String filename) {
        XmlMapper xmlMapper = new XmlMapper();

        PersistentKeyboard keyboard = xmlMapper.readValue(
                new File(PERSISTENT_KEYBOARD_PATH + filename + ".xml"), PersistentKeyboard.class);

        List<CallToAction> categoryList = new ArrayList<>();

        for (PersistentCategory category : keyboard.getCategories()) {
            List<CallToAction> linkList = new ArrayList<>();

            for (PersistentLink link : category.getLinks()) {
                CallbackBuilder callback = CallbackBuilder.createCallback();
                callback.appendAll(keyboard.getController(), link.getPayload());

                linkList.add(PostbackCallToAction.create(link.getText(), callback.toString()));
            }

            categoryList.add(NestedCallToAction.create(category.getTitle(), linkList));
        }

        for (PersistentLink independentLink : keyboard.getIndependentLinks()) {
            CallbackBuilder callback = CallbackBuilder.createCallback();
            callback.appendAll(keyboard.getController(), independentLink.getPayload());

            categoryList.add(PostbackCallToAction.create(independentLink.getText(), callback.toString()));
        }


        return PersistentMenu.create(false, of(categoryList),
                LocalizedPersistentMenu.create(SupportedLocale.zh_CN, false, empty()));
    }

    @Override
    @SneakyThrows
    public List<Button> getButtonKeyboardFromXML(String filename, Long... itemId) {
        XmlMapper xmlMapper = new XmlMapper();

        ButtonKeyboard keyboard = xmlMapper.readValue(
                new File(BUTTON_KEYBOARD_PATH + filename + ".xml"), ButtonKeyboard.class);

        List<Button> buttonList = new ArrayList<>();

        String id = itemId.length == 0 ? "" : itemId[0].toString();

        for (ButtonLink link : keyboard.getLinks()) {
            CallbackBuilder callback = CallbackBuilder.createCallback();
            callback.appendAll(keyboard.getController(), link.getPayload(), id);

            buttonList.add(PostbackButton.create(link.getText(), callback.toString()));
        }

        return buttonList;
    }

    @Override
    @SneakyThrows
    public TemplateMessage getGenericMenuFromXML(String filename, Integer page) {
        XmlMapper xmlMapper = new XmlMapper();

        GenericKeyboard keyboard = xmlMapper.readValue(
                new File(GENERIC_KEYBOARD_PATH + filename + ".xml"), GenericKeyboard.class);

        List<Element> genericItems = new ArrayList<>();

        Long maxPagesList = 0L;

        if (keyboard.getGenericDbItems() != null) {

            for (GenericDbItem item : keyboard.getGenericDbItems()) {
                int maxItemsOnPage = Integer.parseInt(item.getMaxItemsOnPage());
                Class<?> entityClass = Class.forName(KEYBOARD_DAO_PATH + item.getEntity());
                Class<?> imageEntityClass = Class.forName(KEYBOARD_DAO_PATH + item.getImageEntity());

                Method titleMethod = entityClass.getMethod(item.getMethodForTitle());
                Method subtitleMethod = entityClass.getMethod(item.getMethodForSubtitle());
                Method imageMethod = entityClass.getMethod(item.getMethodForImage());
                Method payloadMethod = entityClass.getMethod(item.getMethodForPayload());

                Method imageUrlMethod = imageEntityClass.getMethod(item.getMethodForUrlImage());

                for (Object object : keyboardObjectRepository.getObjectListByEntity(entityClass, page, maxItemsOnPage)) {
                    List<Button> itemButtons = new ArrayList<>();
                    List<HouseImageData> images = new ArrayList<>((List<HouseImageData>) imageMethod.invoke(object));

                    String firstImageUrl = imageUrlMethod.invoke(images.get(0)).toString();

                    for (GenericButton button : item.getButtons()) {
                        CallbackBuilder callback = CallbackBuilder.createCallback();

                        callback.appendAll("GenericMenuItem", button.getPayload(), item.getItemResponseMethod(), payloadMethod.invoke(object));

                        itemButtons.add(PostbackButton.create(button.getText(), callback.toString()));
                    }

                    genericItems.add(
                            Element.create(titleMethod.invoke(object).toString(),
                                    of(subtitleMethod.invoke(object).toString()),
                                    of(new URL(firstImageUrl)),
                                    empty(),
                                    of(itemButtons)));
                }

                Long number = keyboardObjectRepository.getPageNumbersOfObjectListByEntity(entityClass);

                maxPagesList = pagesCount(number, maxItemsOnPage);
            }
        }

        List<QuickReply> quickReplyList = new ArrayList<>();

        if (keyboard.getGenericQuickReplies() != null) {

            for (GenericQuickReply quickReply : keyboard.getGenericQuickReplies()) {

                for(GenericQuickReplyButton button : quickReply.getButtons()) {
                    if (button.isPageable()) {
                        button.setText(button.getText() + " " + (page + 1) + "/" + maxPagesList);
                    }

                    CallbackBuilder callback = CallbackBuilder.createCallback();

                    callback.appendAll(keyboard.getController(), button.getPayload(), page, maxPagesList, filename);

                    quickReplyList.add(TextQuickReply.create(button.getText(), callback.toString()));
                }

            }

        }
        return TemplateMessage.create(GenericTemplate.create(genericItems), of(quickReplyList), empty());
    }

    @Override
    @SneakyThrows
    public TemplateMessage getGenericMenuFromXMLWithRepository(String filename, Integer page, Object... args) {
        XmlMapper mapper = new XmlMapper();

        GenericKeyboard keyboard = mapper.readValue(
                new File(GENERIC_KEYBOARD_PATH + filename + ".xml"), GenericKeyboard.class);

        List<Element> genericItems = new ArrayList<>();

        Long maxPagesList = 0L;

        List<Class> parametersList = new ArrayList<>();
        Object[] arguments = new Object[args.length + 2];

        if (keyboard.getGenericDbItems() != null) {

            for (GenericDbItem item : keyboard.getGenericDbItems()) {
                int maxItemsOnPage = Integer.parseInt(item.getMaxItemsOnPage());

                String entity = item.getEntity();

                Class<?> entityClass = Class.forName(KEYBOARD_DAO_PATH + entity);
                Class<?> imageEntityClass = Class.forName(KEYBOARD_DAO_PATH + item.getImageEntity());
                Class<?> repository = Class.forName(KEYBOARD_REPOSITORY_PATH + item.getRepository());


                Method methodParameters = Arrays.stream(repository.getMethods())
                        .filter(method -> method.getName().equals(item.getRepoMethod()))
                        .findFirst()
                        .get();

                String beanName = item.getRepository().substring(0, 1).toLowerCase() + item.getRepository().substring(1);

                Method repoMethod = repository.getMethod(item.getRepoMethod(), methodParameters.getParameterTypes());

                parametersList.addAll(Arrays.asList(methodParameters.getParameterTypes()));

                Method titleMethod = entityClass.getMethod(item.getMethodForTitle());
                Method subtitleMethod = entityClass.getMethod(item.getMethodForSubtitle());
                Method imageMethod = entityClass.getMethod(item.getMethodForImage());
                Method payloadMethod = entityClass.getMethod(item.getMethodForPayload());

                Method imageUrlMethod = imageEntityClass.getMethod(item.getMethodForUrlImage());

                for (int i = 0; i < args.length; i++) {
                    arguments[i] = args[i];
                }

                arguments[args.length] = page;
                arguments[args.length + 1] = maxItemsOnPage;

                List<Object> objectList = (List<Object>) repoMethod.invoke(applicationContext.getBean(beanName), arguments);

                for (Object object : objectList) {
                    List<Button> itemButtons = new ArrayList<>();
                    List<HouseImageData> images = new ArrayList<>((List<HouseImageData>) imageMethod.invoke(object));

                    String firstImageUrl = imageUrlMethod.invoke(images.get(0)).toString();

                    for (GenericButton button : item.getButtons()) {
                        CallbackBuilder callback = CallbackBuilder.createCallback();
                        callback.appendAll("GenericMenuItem", button.getPayload(), item.getItemResponseMethod(), payloadMethod.invoke(object));
                        itemButtons.add(PostbackButton.create(button.getText(), callback.toString()));
                    }

                    genericItems.add(
                            Element.create(titleMethod.invoke(object).toString(),
                                    of(subtitleMethod.invoke(object).toString()),
                                    of(new URL(firstImageUrl)),
                                    empty(),
                                    of(itemButtons)));
                }

                String countMethodName = "count" + item.getRepoMethod().substring(item.getRepoMethod().indexOf("By"));

                Class[] countParameters = Arrays.asList(repoMethod.getParameterTypes()).subList(0, args.length).toArray(Class[]::new);

                Method countRepoMethod = repository.getMethod(countMethodName, countParameters);

                Long number = (Long) countRepoMethod.invoke(applicationContext.getBean(beanName), args);

                maxPagesList = pagesCount(number, maxItemsOnPage);
            }
        }

        List<QuickReply> quickReplyList = new ArrayList<>();

        if (keyboard.getGenericQuickReplies() != null) {

            for (GenericQuickReply quickReply : keyboard.getGenericQuickReplies()) {

                for(GenericQuickReplyButton button : quickReply.getButtons()) {
                    if (button.isPageable()) {
                        button.setText(button.getText() + " " + (page + 1) + "/" + maxPagesList);
                    }

                    CallbackBuilder callback = CallbackBuilder.createCallback();
                    callback.appendAll(keyboard.getController(), button.getPayload(), page, maxPagesList, filename, parametersList, Arrays.toString(args));


                    quickReplyList.add(TextQuickReply.create(button.getText(), callback.toString()));
                }

            }

        }
        return TemplateMessage.create(GenericTemplate.create(genericItems), of(quickReplyList), empty());
    }

    @Override
    @SneakyThrows
    public List<QuickReply> getQuickReplyKeyboardFromXML(String filename) {
        XmlMapper xmlMapper = new XmlMapper();

        QuickReplyKeyboard keyboard = xmlMapper.readValue(
                new File(QUICK_REPLY_KEYBOARD_PATH + filename + ".xml"), QuickReplyKeyboard.class);

        List<QuickReply> quickReplyList = new ArrayList<>();

        for (QuickReplyButton quickReply : keyboard.getQuickReplies()) {
            CallbackBuilder callback = CallbackBuilder.createCallback();
            callback.appendAll(keyboard.getController(), quickReply.getPayload());

            quickReplyList.add(TextQuickReply.create(quickReply.getText(), callback.toString()));
        }

        return quickReplyList;
    }

    private Long pagesCount(Long number, int maxItemsOnPage) {
        int pageIncrementer = number % maxItemsOnPage == 0 ? 0 : 1;
        return (number / maxItemsOnPage + pageIncrementer);
    }
}
