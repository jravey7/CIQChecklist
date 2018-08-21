using Toybox.WatchUi as Ui;

class CIQChecklistDelegate extends Ui.BehaviorDelegate {

    function initialize() {
        BehaviorDelegate.initialize();
    }

    function onMenu() {
        Ui.pushView(new Rez.Menus.MainMenu(), new CIQChecklistMenuDelegate(), Ui.SLIDE_UP);
        return true;
    }

}