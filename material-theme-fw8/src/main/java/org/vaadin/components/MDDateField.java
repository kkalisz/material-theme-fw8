package org.vaadin.components;

import com.vaadin.data.HasValue;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import org.vaadin.style.MaterialIcons;
import org.vaadin.style.Styles;

import java.time.LocalDate;

import static org.vaadin.style.Styles.TextFields.FloatingLabel.RESTING;

/**
 * Created by jonte on 15/03/2017.
 */
public class MDDateField extends CssLayout {

    private static final long serialVersionUID = 1L;

    private Label label = new Label();
    private Label icon = new Label();
    private Label helper = new Label();
    private boolean floatingLabelEnabled;
    private String helperText;
    private DateField field = new DateField() {
        @Override
        public void setComponentError(ErrorMessage componentError) {
            super.setComponentError(componentError);
            setError(componentError == null ? null : ((AbstractErrorMessage) componentError).getMessage());
        }
    };

    public MDDateField(String label) {
        this(label, true);
    }

    public MDDateField(String label, boolean light) {
        String primaryStyleName = light ? Styles.DateFields.LIGHT : Styles.DateFields.DARK;
        setPrimaryStyleName(primaryStyleName);
        setFloatingLabelEnabled(true);

        this.label.setValue(label);
        this.label.setPrimaryStyleName(primaryStyleName + "-label");
        this.label.addStyleName(RESTING);
        this.label.setWidthUndefined();

        this.icon.setPrimaryStyleName(primaryStyleName + "-icon");
        this.icon.setContentMode(ContentMode.HTML);
        this.icon.setVisible(false);

        this.field.setPrimaryStyleName(primaryStyleName + "-input");
        this.field.addFocusListener(event -> {
            addStyleName("focus");
            updateFloatingLabelPosition(this.field.getValue());

        });
        this.field.addBlurListener(event -> {
            removeStyleName("focus");
            updateFloatingLabelPosition(this.field.getValue());
        });

        this.field.addValueChangeListener(event -> updateFloatingLabelPosition(event.getValue()));

        this.helper.setPrimaryStyleName(primaryStyleName + "-helper");
        this.helper.setWidthUndefined();

        addComponents(this.label, icon, field, this.helper);
    }

    public DateField getField() {
        return field;
    }

    public String getLabel() {
        return label.getValue();
    }

    public void setLabel(String label) {
        this.label.setValue(label);
    }

    public String getHelper() {
        return this.helper.getValue();
    }

    public void setHelper(String text) {
        this.helperText = text;

        removeStyleName("error");
        this.helper.setValue(helperText);
    }

    public void setError(String text) {
        if (text == null) {
            removeStyleName("error");
            this.helper.setValue(helperText);
        } else {
            addStyleName("error");
            this.helper.setValue(text);
        }
    }

    public void setFloatingLabelEnabled(boolean enabled) {
        this.floatingLabelEnabled = enabled;

        if (enabled) {
            removeStyleName("float-disabled");
        } else {
            addStyleName("float-disabled");
        }
    }

    public void setIcon(MaterialIcons icon) {
        if (icon == null) {
            this.icon.setVisible(false);
            removeStyleName("with-icon");
        } else {
            addStyleName("with-icon");
            this.icon.setVisible(true);
            this.icon.setValue(icon.getHtml());
        }
    }

    public Registration addValueChangeListener(HasValue.ValueChangeListener<LocalDate> listener) {
        return field.addValueChangeListener(listener);
    }

    @Override
    public void setComponentError(ErrorMessage componentError) {
        field.setComponentError(componentError);
    }

    public void setValue(LocalDate value) {
        this.field.setValue(value);
        updateFloatingLabelPosition(value);
    }

    private void updateFloatingLabelPosition(LocalDate value) {
        // Focused
        if (getStyleName().contains("focus")) {

            // Floating
            if (floatingLabelEnabled) this.label.removeStyleName(RESTING);

            // Non-floating
            else this.label.addStyleName(Styles.TextFields.FloatingLabel.HIDE);
        }

        // Not focused
        else {

            // Floating
            if (floatingLabelEnabled) {
                if (value == null) this.label.addStyleName(RESTING);
                else this.label.removeStyleName(Styles.TextFields.FloatingLabel.RESTING);
            }

            // Non-floating
            else {
                if (value == null) this.label.removeStyleName(Styles.TextFields.FloatingLabel.HIDE);
                else this.label.addStyleName(Styles.TextFields.FloatingLabel.HIDE);
            }
        }
    }

}
