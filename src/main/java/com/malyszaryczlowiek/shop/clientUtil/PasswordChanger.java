package com.malyszaryczlowiek.shop.clientUtil;

import javax.validation.constraints.Pattern;

public class PasswordChanger {

    @Pattern(regexp = ".{9,}", message = "New password must contain nine characters at least.")
    private String newPass;

    private String oldPass;

    public PasswordChanger() {}

    public PasswordChanger(
            @Pattern(regexp = ".{9,}",
                    message = "New password must contain nine characters at least.") String newPass,
            String oldPass) {
        this.newPass = newPass;
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(
            @Pattern(regexp = ".{9,}",
                    message = "New password must contain nine characters at least.") String newPass) {
        this.newPass = newPass;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }
}
