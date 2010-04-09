document.observe('dom:loaded', function()
{
    $('expand-extra-charts').observe('click', function(event) {
        if ($('extra-charts').visible()) {
            $('expand-extra-charts').update("+");
            $('extra-charts').hide();
        } else {
            $('expand-extra-charts').update("-");
            $('extra-charts').show();
        }
    });

    $('expand-card-pictures').observe('click', function(event) {
        if ($('card-pictures').visible()) {
            $('expand-card-pictures').update("+");
            $('card-pictures').hide();
        } else {
            $('expand-card-pictures').update("-");
            $('card-pictures').show();
        }
    });
});