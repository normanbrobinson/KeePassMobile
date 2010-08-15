/*
    Copyright (c) 2009-2010 Christoph Sperle <keepassmobile@gmail.com>
    
    This file is part of KeePassMobile.

    KeePassMobile is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    KeePassMobile is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with KeePassMobile.  If not, see <http://www.gnu.org/licenses/>.

*/

package org.sperle.keepass.ui.source.file;


import org.sperle.keepass.ui.KeePassMobile;
import org.sperle.keepass.ui.form.Forms;
import org.sperle.keepass.ui.form.KeePassMobileForm;
import org.sperle.keepass.ui.i18n.Messages;

import com.sun.lwuit.Command;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BoxLayout;

public class MasterPasswordForm extends KeePassMobileForm {
    final FileSource fileSource;
    
    private Label passwdLabel;
    private TextArea passwdField;
    private Command defaultCommand;
    
    public MasterPasswordForm(final KeePassMobile app, final FileSource fileSource) {
        super(app, fileSource.getKdbName());
        this.fileSource = fileSource;
        
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        setScrollable(false);
        
        app.getCommandManager().addCommands(this, createCommands(), defaultCommand);
        
        passwdLabel = new Label(Messages.get("enter_password"));
        addComponent(passwdLabel);
        passwdField = new TextArea();
        if(!app.isPasswordFieldWorkaroundEnabled()) {
            passwdField.setConstraint(TextArea.ANY | TextArea.PASSWORD);
        }
        addComponent(passwdField);
    }
    
    private Command[] createCommands() {
        Command[] commands = new Command[4];
        commands[0] = new Command(Messages.get("open")) {
            public void actionPerformed(ActionEvent evt) {
                fileSource.decrypt(passwdField.getText(), null);
            }
        };
        commands[1] = new Command(Messages.get("use_keyfile")) {
            public void actionPerformed(ActionEvent evt) {
                fileSource.openKeyFile(passwdField.getText());
            }
        };
        commands[2] = new Command(Messages.get("help")) {
            public void actionPerformed(ActionEvent evt) {
                Forms.showHelp(Messages.get("masterpassword_help"));
            }
        };
        commands[3] = new Command(Messages.get("cancel")) {
            public void actionPerformed(ActionEvent evt) {
                goBack();
            }
        };
        
        defaultCommand = commands[0]; // open
        return commands;
    }
    
    protected void goBack() {
        fileSource.passwordEnteringCanceled();
    }
}
