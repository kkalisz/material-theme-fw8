package org.vaadin.components;

import com.vaadin.data.HasValue;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.IconGenerator;
import com.vaadin.ui.Label;
import org.vaadin.style.MaterialIcons;
import org.vaadin.style.Styles;

import java.util.Collection;

import static org.vaadin.style.Styles.TextFields.FloatingLabel.RESTING;

/**
 * Created by jonte on 15/03/2017.
 */
public class MDComboBox<T> extends CssLayout {

    private static final long serialVersionUID = 1L;

    private MaterialIcons defaultIcon;

    private Label label = new Label();
    private Label icon = new Label();
    private Label helper = new Label();
    private boolean floatingLabelEnabled;
    private String helperText;
    private ComboBox<T> field;

    public MDComboBox(String label) {
        this(label, true);
    }

    public MDComboBox(String label, boolean light) {
        String primaryStyleName = light ? Styles.ComboBoxes.LIGHT : Styles.ComboBoxes.DARK;
        setPrimaryStyleName(primaryStyleName);

        this.label.setValue(label);
        this.label.setPrimaryStyleName(primaryStyleName + "-label");
        this.label.addStyleName(RESTING);
        this.label.setWidthUndefined();

        this.icon.setPrimaryStyleName(primaryStyleName + "-icon");
        this.icon.setContentMode(ContentMode.HTML);
        this.icon.setVisible(false);

        field = new ComboBox<T>() {
            @Override
            public void setComponentError(ErrorMessage componentError) {
                super.setComponentError(componentError);
                setError(componentError == null ? null : ((AbstractErrorMessage) componentError).getMessage());
            }
        };
        this.field.setPrimaryStyleName(primaryStyleName + "-input");
        this.field.addFocusListener(event -> {
            addStyleName("focus");
            updateFloatingLabelPosition(this.field.getValue());

        });
        this.field.addBlurListener(event -> {
            removeStyleName("focus");
            updateFloatingLabelPosition(this.field.getValue());
        });
        this.field.addValueChangeListener(event -> {
            if (this.field.getItemIconGenerator() == null) return;
            setIcon(this.field.getValue() == null ? null : (MaterialIcons) this.field.getItemIconGenerator().apply(this.field.getValue()), false);
            updateFloatingLabelPosition(event.getValue());
        });

        this.helper.setPrimaryStyleName(primaryStyleName + "-helper");
        this.helper.setWidthUndefined();

        addComponents(this.label, icon, field, this.helper);

        setFloatingLabelEnabled(true);
    }

    public ComboBox<T> getField() {
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

    public void setEmptySelectionAllowed(boolean emptySelectionAllowed) {
        this.field.setEmptySelectionAllowed(emptySelectionAllowed);
    }

    public void setFloatingLabelEnabled(boolean enabled) {
        this.floatingLabelEnabled = enabled;

        if (enabled) {
            removeStyleName("float-disabled");
        } else {
            addStyleName("float-disabled");
        }

        this.field.setItems();
    }

    public void setIcon(MaterialIcons icon) {
        setIcon(icon, true);
    }

    public void setIcon(MaterialIcons icon, boolean def) {
        if (def) {
            this.defaultIcon = icon;
        }
        if (defaultIcon != null || icon != null) {
            addStyleName("with-icon");
            this.icon.setVisible(true);
            this.icon.setValue(icon != null ? icon.getHtml() : defaultIcon.getHtml());
        } else {
            removeStyleName("with-icon");
            this.icon.setVisible(false);
        }
    }

    public Registration addValueChangeListener(HasValue.ValueChangeListener<T> listener) {
        return field.addValueChangeListener(listener);
    }

    @Override
    public void setComponentError(ErrorMessage componentError) {
        field.setComponentError(componentError);
    }

    public void setItems(Collection<T> items) {
        this.field.setItems(items);
    }

    public void setValue(T value) {
        this.field.setValue(value);
        updateFloatingLabelPosition(value);
    }

    public void setSelectedItem(T item) {
        this.field.setSelectedItem(item);
        updateFloatingLabelPosition(item);
    }

    public void setItemIconGenerator(IconGenerator itemIconGenerator) {
        this.field.setItemIconGenerator(itemIconGenerator);
    }

    private void updateFloatingLabelPosition(T value) {
        // Focused
        if (getStyleName().contains("focus")) {

            // Floating
            if (floatingLabelEnabled) this.label.removeStyleName(Styles.TextFields.FloatingLabel.RESTING);

                // Non-floating
            else this.label.addStyleName(Styles.TextFields.FloatingLabel.HIDE);
        }

        // Not focused
        else {

            // Floating
            if (floatingLabelEnabled) {
                if (value == null) this.label.addStyleName(Styles.TextFields.FloatingLabel.RESTING);
                else this.label.removeStyleName(Styles.TextFields.FloatingLabel.RESTING);
            }

            // Non-floating
            else {
                if (value == null) this.label.removeStyleName(Styles.TextFields.FloatingLabel.HIDE);
                else this.label.addStyleName(Styles.TextFields.FloatingLabel.HIDE);
            }
        }
    }

    public void setTextInputAllowed(boolean textInputAllowed) {
        this.field.setTextInputAllowed(textInputAllowed);
    }

}
