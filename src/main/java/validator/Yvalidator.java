package validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("y_validator")
public class Yvalidator implements Validator {
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {

        try {
            double input = (Double) o;
            try {
                if (input >= 5. || input <= -3.) {
                    FacesMessage message = new FacesMessage("Incorrect Y value");
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(message);
                }
            } catch (NumberFormatException e) {
                FacesMessage message = new FacesMessage("Incorrect Y value");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(message);
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

}
