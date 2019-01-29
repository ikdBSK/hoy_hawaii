$(function() {
    var Accordion = function(el, multiple) {
        this.el = el || {};
        // more then one submenu open?
        this.multiple = multiple || false;

        var dropdownlink = this.el.find('.dropdownlink');
        dropdownlink.on('click',
            { el: this.el, multiple: this.multiple },
            this.dropdown);
    };

    Accordion.prototype.dropdown = function(e) {
        var $el = e.data.el,
            $this = $(this),
            //this is the ul.submenuItems
            $next = $this.next();

        $next.slideToggle();
        $this.parent().toggleClass('open');

        if(!e.data.multiple) {
            //show only one menu at the same time
            $el.find('.submenuItems').not($next).slideUp().parent().removeClass('open');
        }
    };

    var accordion = new Accordion($('.accordion-menu'), false);
});

function open_admin_menu(id) {
    var $ = jQuery;
    $('#admin_screen .content >div').each(function(i, o){
        $(o).hide();
    });
    $(id).show();
}

function open_student_menu(id) {
    var $ = jQuery;
    $('#student_screen .content >div').each(function(i, o){
        $(o).hide();
    });
    $(id).show();
}

function open_teacher_menu(id) {
    var $ = jQuery;
    $('#teacher_screen .content >div').each(function(i, o){
        $(o).hide();
    });
    $(id).show();
}
