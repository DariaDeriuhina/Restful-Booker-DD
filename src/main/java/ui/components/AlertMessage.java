package ui.components;

import com.codeborne.selenide.ElementsCollection;
import ui.elements.BaseElement;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

public class AlertMessage extends BaseElement {

    public AlertMessage() {
        body = $(".alert.alert-danger");
    }

    public ElementsCollection messages() {
        return body.$$("p, li");
    }

    public void shouldHaveMessages(List<String> expectedMessages) {
        var actualTexts = messages().texts();
        assertThat(actualTexts).containsExactlyInAnyOrderElementsOf(expectedMessages);
    }
}
