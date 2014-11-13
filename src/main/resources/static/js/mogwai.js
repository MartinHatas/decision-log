//DO NOT EDIT - file generated via 'webui' tool
//  Original file was modified to work with GMC Webui Framework
/* ============================================================
 * bootstrap-dropdown.js v2.2.2
 * http://twitter.github.com/bootstrap/javascript.html#dropdowns
 * ============================================================
 * Copyright 2012 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============================================================ */


!function ($) {

  "use strict"; // jshint ;_;


 /* DROPDOWN CLASS DEFINITION
  * ========================= */
  var Dropdown = function (element) {
        // var $el = $(element).on('click.dropdown.data-api', this.toggle)
        // $('html').on('click.dropdown.data-api', function () {
        //   $el.parent().removeClass('is-open')
        // })
      }

  Dropdown.prototype = {

    constructor: Dropdown

  , toggle: function (e, options) {
    //console.log("toggle");
      var $this = $(this)
        , $parent
        , isActive

      if ($this.is('.is-disabled, :disabled')) return

      $parent = getParent($this, options)

      isActive = $parent.hasClass('is-open')

      clearMenus()

      if (!isActive) {
        $parent.toggleClass('is-open')
      }

      $this.focus()

      return false
    }

  , keydown: function (e, options) {
      var $this
        , $items
        , $active
        , $parent
        , isActive
        , index

      if (!/(38|40|27)/.test(e.keyCode)) return

      $this = $(this)

      e.preventDefault()
      e.stopPropagation()

      if ($this.is('.is-disabled, :disabled')) return

      $parent = getParent($this, options)

      isActive = $parent.hasClass('is-open')

      if (!isActive || (isActive && e.keyCode == 27)) return $this.click()

      $items = $('[role=menu] li:not(.divider):visible a', $parent)

      if (!$items.length) return

      index = $items.index($items.filter(':focus'))

      if (e.keyCode == 38 && index > 0) index--                                        // up
      if (e.keyCode == 40 && index < $items.length - 1) index++                        // down
      if (!~index) index = 0

      $items
        .eq(index)
        .focus()
    }

  }

  function clearMenus() {
    //console.log("clear");
    var data = $(document).data("activateDropdown");
    for (var i = 0; i < data.length; i++) {
      var options = data[i];
      $(options.toggleSelector).each(function () {
        getParent($(this), options).removeClass('is-open')
      })
    };
  }

  function getParent($this, options) {
    var $parent = $this.closest(options.parentSelector)
    $parent.length || ($parent = $this.parent())

    return $parent
  }


  /* DROPDOWN PLUGIN DEFINITION
   * ========================== */

  // var old = $.fn.dropdown

  // $.fn.dropdown = function (option) {
  //   return this.each(function () {
  //     var $this = $(this)
  //       , data = $this.data('dropdown')
  //     if (!data) $this.data('dropdown', (data = new Dropdown(this)))
  //     if (typeof option == 'string') data[option].call($this)
  //   })
  // }

  // $.fn.dropdown.Constructor = Dropdown


 /* DROPDOWN NO CONFLICT
  * ==================== */

  // $.fn.dropdown.noConflict = function () {
  //   $.fn.dropdown = old
  //   return this
  // }


  /* APPLY BEHAVIOR MANUALY
   * =================================== */

  $.fn.activateDropdown = function(options) {
    var $document = $(document);
    var data = $document.data('activateDropdown');
    if (!data) {
      $document.data('activateDropdown', [options]);
      $document
        .on('click.dropdown.data-api touchstart.dropdown.data-api', clearMenus)
        .on('click.dropdown touchstart.dropdown.data-api', '.dropdown form', function (e) { e.stopPropagation() })
        .on('touchstart.dropdown.data-api', '.dropdown-menu', function (e) { e.stopPropagation() })
    } else {
      data.push(options);
    }
    
    function getToggleFunction(options) {
      return function(e) {
        return Dropdown.prototype.toggle.call(this, e, options); }
    }

    function getKeyDownFunction(options) {
      return function(e) {
        return Dropdown.prototype.keydown.call(this, e, options); }
    }

    $document
      .on('click.dropdown.data-api touchstart.dropdown.data-api'  , options.toggleSelector, getToggleFunction(options))
      .on('keydown.dropdown.data-api touchstart.dropdown.data-api', options.toggleSelector + ', [role=menu]' , getKeyDownFunction(options))
    return this;
  }

}(window.jQuery);
// moves elements which are using css fixed position when window scrolled (to be able to set min widtch in app-header)
//http://stackoverflow.com/questions/14879435/css-scrolling-on-both-html-element-and-fixed-element-with-min-width
!function ($) {
  $.fn.fixedElementHorizontalScroll = function(options) {
    var prevOffsetLeft = $(window).scrollLeft();
    $(window).scroll(function() {
        var offsetLeft = $(window).scrollLeft();
        if (prevOffsetLeft == offsetLeft) {
            return;
        }
        prevOffsetLeft = offsetLeft;

        // console.log('doing scroll watch');
        $(options.selector).each(function() {
            $fixedElement = $(this);

            if ($fixedElement.data('originalLeft') == null) {
                // css.left has string value (e.g. '50px')
                var left = parseInt($fixedElement.css('left'));
                if (!left) {
                    left = 0;
                }
                $fixedElement.data('originalLeft', left);
            }
            var originalLeft = $fixedElement.data('originalLeft');
            //- console.log($fixedElement, originalLeft);
            $fixedElement.css('left', originalLeft - offsetLeft);
        });
    });
    return this;
  }
}(window.jQuery);
//toggles focus for inner input element setting .is-focused on parent
!function ($) {
  var FocusableField = function () {};
  FocusableField.prototype = {
    constructor: FocusableField,
    focus: function() {
      var $this = $(this);
      //console.log('focus in this', this);
      $this.addClass("is-focused");
    },
    focusout: function() {
      var $this = $(this);
      //console.log('focusout in this', this);
      $this.removeClass("is-focused");
    },
    click: function() {
      var $this = $(this);
      //console.log('click in this', this);
      $this.find("input").focus();
    }
  }

  /* APPLY BEHAVIOR MANUALY
   * =================================== */

  $.fn.activateFocusableField = function(options) {
    var selector = options.focusableParentSelector;
    $(document)
      .on('focus.focusable-field.data-api' , selector, FocusableField.prototype.focus)
      .on('focusout.focusable-field.data-api' , selector, FocusableField.prototype.focusout)
      .on('click.focusable-field.data-api' , selector, FocusableField.prototype.click)

    return this;
  }

}(window.jQuery);
$(window).fixedElementHorizontalScroll({
    selector: '.l-view-app-header'
});
$(window).fixedElementHorizontalScroll({
    selector: '.l-view-notification'
});
$(window).fixedElementHorizontalScroll({
    selector: '.l-view-sidebar'
});
$(window).fixedElementHorizontalScroll({
    selector: '.l-view-task-queue'
});
!function ($) {
	var TableCollapsibleRow = function () {};
	TableCollapsibleRow.prototype = {
		constructor: TableCollapsibleRow,
		toggle: function(e) {
			var $row = $(this).closest(".table-collapsible-row-header");

			if ($row.is(".is-open")) {
				$row.removeClass("is-open");
			} else {
				//$(".table-collapsible-row-heading").removeClass("is-open");
				$row.addClass("is-open");
			}

			$row.closest("table").addClass("table-collapsible-row-ie8-table-reflow");
		}
	}

	$(document)
		.on('click.table-collapsible-row.data-api touchstart.table-collapsible-row.data-api' , ".table-collapsible-row-toggle", TableCollapsibleRow.prototype.toggle);
}(window.jQuery);
!function ($) {
    var TableSelectableRow = function () {};
    TableSelectableRow.prototype = {
        constructor: TableSelectableRow,
        select: function(e) {
            var $row = getRow($(this));

            if ($row.is(".is-selected")) {
                var rowData = $row.data("table-selectable-row");
                if (rowData && rowData.hoverSelection) {
                    rowData.hoverSelection = false;
                    clear();
                    selectRow($row);
                }
            } else {
                clear();
                selectRow($row);
            }
        },
        mouseEnter: function(e) {
            var $row = getRow($(this));
            if (!$row.is(".is-selected")) {
                selectRow($row);
                $row.data("table-selectable-row", {hoverSelection: true});
            }
        },
        mouseLeave: function(e) {
            var $row = getRow($(this));
            var rowData = $row.data("table-selectable-row");
            if (rowData && rowData.hoverSelection) {
                unselectRow($row);
            }
        },
        clearNoRow: function(e) {
            var $target = $(e.target);
            var rowSelected = $target.is(".table-selectable-row") || ($target.closest(".table-selectable-row").size() > 0);
            if (!rowSelected) {
                clear();
            }
        }
    }

    function clear() {
        $(".table-selectable-row").removeClass("is-selected");
    }

    function getRow($rowOrJoined) {
        if ($rowOrJoined.is(".table-selectable-row-joined")) {
            return $rowOrJoined.prev();
        }
        return $rowOrJoined;
    }

    function selectRow($row) {
        $row.addClass("is-selected");
        $row.next(".table-selectable-row-joined").addClass("is-selected");
    }
    function unselectRow($row) {
        $row.removeClass("is-selected");
        $row.next(".table-selectable-row-joined").removeClass("is-selected");
    }

    $(document)
        .on('click.table-selectable-row.data-api touchstart.table-selectable-row.data-api' , TableSelectableRow.prototype.clearNoRow)
        .on('click.table-selectable-row.data-api touchstart.table-selectable-row.data-api' , ".table-selectable-row", TableSelectableRow.prototype.select)
        .on('mouseenter.table-selectable-row.data-api' , ".table-selectable-row", TableSelectableRow.prototype.mouseEnter)
        .on('mouseleave.table-selectable-row.data-api' , ".table-selectable-row", TableSelectableRow.prototype.mouseLeave)
        
}(window.jQuery);
$(document).activateFocusableField({
	focusableParentSelector: ".textbox"
});
