document.observe('dom:loaded', function()
{
    /*
     * Initialize the asynchronous suggestion mechanism.
     */
    new Ajax.Autocompleter('search-input', 'suggestion-area',
        '${rc.getContextPath()}/services/card/suggestion',
    {
        afterUpdateElement: function () {}
    });
});
