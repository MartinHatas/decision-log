/**
 * Created by m.jankovsky on 14.11.2014.
 */
jQuery.noConflict();
(function ($) {
    $(function () {
        $( "body" ).keypress(function(event) {
            if (event.charCode > 47 && event.charCode <58)
            {
                event.preventDefault();
                if(window.console) console.log("key 0-9 prevent - for demo - keys reserved");
                if (event.charCode == 49){
                    jQuery("#subject").val("Modularization - Architecture change");
                }
                if (event.charCode ==50){
                    jQuery("#reason").val("We want to split our monolithic application into a modules so that we support code ownership that bring us more code quality a faster development.");
                }
                if (event.charCode ==51){
                    jQuery("#conclusions").val("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Nam sed tellus id magna elementum tincidunt. Maecenas aliquet accumsan leo. Mauris dolor felis, sagittis at, luctus sed, aliquam non, tellus.");
                }
                if (event.charCode ==52){
                    jQuery("#attendees").val("Denis van Gaal, Don Ruiz, Frank Bucha");
                }
                if (event.charCode ==53){
                    jQuery("#tags").val("modularization, server, ownership, quality");
                }
            }

            console.log(event);
        });
    });
})(jQuery);