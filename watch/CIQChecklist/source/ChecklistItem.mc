using Toybox.WatchUi;

class ChecklistItem extends WatchUi.CheckboxMenuItem {

	function initialize(label, sublabel, identifier, checked)
	{
		var options = {:alignment=>WatchUi.MenuItem.MENU_ITEM_LABEL_ALIGN_LEFT};
		CheckboxMenuItem.initialize(label, sublabel, identifier, checked, options);
	}
}