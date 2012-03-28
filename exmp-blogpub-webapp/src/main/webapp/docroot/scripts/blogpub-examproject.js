/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

///////////////////////////////////////////////////////////////////////////////
/**
 * a functor class of the application.
 * send HTTP request for the entry post.
 */
var EntryPostClosure = {
    execute: function(entry) {
        WaitingMessageClosure.execute(
            "please wait..."
        );
        new $.ajax({
            url: "post.html",
            type: "POST",
            data: entry,
            dataType: "json",
            contentType: "application/json;charset=UTF-8",
            success: function(data, dataType) {
                if (data.isError) {
                    ErrorMessageClosure.execute(
                        "application error occurred.."
                    );
                    return;
                }
                // update the entry list HTML table. 
                window.updateTables(data, dataType);
                $("#entry_title").val("");
                $("#entry_content").val("");
                SuccessMessageClosure.execute(
                    "complete."
                );
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                ErrorMessageClosure.execute(
                    "httprequest error occurred.."
                );
            }
        });
    }
}

///////////////////////////////////////////////////////////////////////////////
/**
 * a functor class of the application.
 * send HTTP request for get the entry list.
 */
var EntryListClosure = {
    execute: function(entry) {
        ListWaitingClosure.execute(
            null
        );
        new $.ajax({
            url: "list.html",
            type: "POST",
            data: entry,
            dataType: "json",
            contentType: "application/json;charset=UTF-8",
            success: function(data, dataType) {
                if (data.isError) {
                    ErrorMessageClosure.execute(
                        "application error occurred.."
                    );
                    $("#entry-list-block").html("");
                    return;
                }
                window.updateTables(data, dataType);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                ErrorMessageClosure.execute(
                    "httprequest error occurred.."
                );
                $("#entry-list-block").html("");
            }
        });
    }
}

///////////////////////////////////////////////////////////////////////////////
/**
 * a functor class of the application.
 * send HTTP request for the setting.
 */
var SettingClosure = {
    execute: function(entry) {
        WaitingMessageClosure.execute(
            "please wait..."
        );
        new $.ajax({
            url: "setting.html",
            type: "POST",
            data: entry,
            dataType: "json",
            contentType: "application/json;charset=UTF-8",
            success: function(data, dataType) {
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                // because this Ajax requests are redirected as normal.
                if (XMLHttpRequest.status == 200) {
                    SuccessMessageClosure.execute(
                        "complete."
                    );
                    return;
                }
                ErrorMessageClosure.execute(
                    "httprequest error occurred.."
                );
            }
        });
    }
}

///////////////////////////////////////////////////////////////////////////////
/**
 * a functor class of the application.
 * this class is a transformer that JSON data get by
 * Ajax HTTP requests and convert to HTML tables.
 */
var EntryListTransformer = {
    transform: function(data) {
        var table = "<table class='entry-list-table'>";
        for (var i = 0; i < data.entryList.length; i++) {
            var title = data.entryList[i].title;
            var content = data.entryList[i].content;
            title = $.erasureHTML(title);
            content = $.erasureHTML(content);
            table +=
                "<tr>" +
                    "<td class='entry-list-td' >" + title + " " + content + "</td>" +
                "</tr>";
        }
        table += "</table>";
        return table;
    }
}

///////////////////////////////////////////////////////////////////////////////
/**
 * a functor class of the application.
 * get the value from the HTML form and 
 * create a JSON object for HTTP POST.
 */
var EntryFactory = {
    create: function() {
        // convert the form data to JSON.
        var param = {};
        $($("#entry-form").serializeArray()).each(
            function(i, v) {
                param[v.name] = v.value;
            }
        );
        return $.toJSON(param);
    }
};

///////////////////////////////////////////////////////////////////////////////
/**
 * a functor class of the application.
 * display a message to div when the command is success.
 */
var SuccessMessageClosure = {
    execute: function(message) {
        $("#message-block")
        .addClass("show")
        .css("background-color", "lightskyblue")
        .css("padding", "0.25em")
        .html(
            '<img id="message_close_img" src="../docroot/images/close.png" width="16" height="16" />' +
            "<p>" + message + "</p>"
        );
    }
}

///////////////////////////////////////////////////////////////////////////////
/**
 * a functor class of the application.
 * display a message to div when the command is error.
 */
var ErrorMessageClosure = {
    execute: function(message) {
        $("#message-block")
        .addClass("show")
        .css("background-color", "lightpink")
        .css("padding", "0.25em")
        .html(
            '<img id="message_close_img" src="../docroot/images/close.png" width="16" height="16" />' +
            "<p>" + message + "</p>"
        );
    }
}

///////////////////////////////////////////////////////////////////////////////
/**
 * a functor class of the application.
 * display a message to div when an ajax http request command waiting.
 */
var WaitingMessageClosure = {
    execute: function(message) {      
        $("#message-block")
        .addClass("show")
        .css("background-color", "gainsboro")
        .css("padding", "0.25em")
        .html(
            '<p><img src="../docroot/images/loading.gif" width="31" height="31" /></p>' + 
            "<p>" + message + "</p>"
        );
    }
}

///////////////////////////////////////////////////////////////////////////////
/**
 * a functor class of the application.
 * display an icon image when an ajax http request of
 * to get the entry list is waiting.
 */
var ListWaitingClosure = {
    execute: function(message) {
        $("#entry-list-block").html(
            '<p><img src="../docroot/images/loading.gif" width="31" height="31" alt="now loading..." /></p>'
        );
    }
}

///////////////////////////////////////////////////////////////////////////////
/**
 * a view class of the application.
 */
var View = window; {
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    /**
     * the initialization method of the View class.
     * this method should be called.
     */
    View.init = function() {
        View._initializeComponent();
    }

    /**
     * update the entry list HTML table.
     */
    View.updateTables = function(data, dataType) {
        $("#entry-list-block").html(
            EntryListTransformer.transform(
                data
            )
        );
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // event handler methods
    
    /**
     * an event handler that called when 
     * the button of post is clicked.
     */
    View._postButtonOnClick = function() {
        EntryPostClosure.execute(
            EntryFactory.create()
        );
    }
        
    /**
     * an event handler that called when 
     * the button of setting is clicked.
     */
    View._settingButtonOnClick = function() {
        SettingClosure.execute(
            EntryFactory.create()
        );
    }

    /**
     * an event handler that called when 
     * the div of message close is clicked.
     */
    View._messageDivOnClick = function() {
        $("#message-block")
        .removeClass("show")
        .css("padding", "0")
        .html("");
    }
    
    /**
     * an event handler that called when 
     * the div of entry tirle is clicked.
     */
    View._entryContentTitleDivOnClick = function() {
        $("div.container").toggleClass("wide", 300);
    }
        
    ///////////////////////////////////////////////////////////////////////////
    // private methods
    
    /**
     * initializes a div of the tabs area.
     */
    View._initializeTabsDiv = function() {
        $("div.tab-content div.tab").hide();
        $("div.tab-content div.tab:first").show();
        $("div.tab-content ul li:first").addClass("active");	 
        $("div.tab-content ul li a").click(function(){
            $("div.tab-content ul li").removeClass("active");
            $(this).parent().addClass("active");
            var currentTab = $(this).attr("href");
            $("div.tab-content div.tab").hide();
            $(currentTab).show();
            return false;
        });
    }

    /**
     * initializes a div of entry list.
     * a HTTP request of Ajax for get the entry data.
     */
    View._initializeEntryListDiv = function() {
        // sorry last resort...
        // TODO: If a session is added... ';' in the split?
        var pageUrl = location.href.split('/');        
        if ((pageUrl[pageUrl.length - 2] + "/" + pageUrl[pageUrl.length - 1])
            == "entry/form.html") {
            EntryListClosure.execute(
                EntryFactory.create()
            );
        }
    }
    
    /**
     * initializes a category select of form.
     * TODO: a HTTP request of Ajax for get the data.. ? 
     */
    View._initializeCategorySelect = function() {       
        $("#entry_category").append($('<option value="General">General</option>'));
        $("#entry_category").append($('<option value="Technology">Technology</option>'));
        $("#entry_category").append($('<option value="Language">Language</option>'));
        $("#entry_category").append($('<option value="Music">Music</option>'));
        $("#entry_category").append($('<option value="Status">Status</option>'));
        $("#entry_category").val("General");
    }
    
    /**
     * initialize a component of the view class.
     */
    View._initializeComponent = function() {
        
        // calls for the initialization methods.
        
        View._initializeEntryListDiv();

        View._initializeTabsDiv();
        
        View._initializeCategorySelect();

        // set the control's event handler.
        
        $("#post-button").click(function() {
            View._postButtonOnClick();
        });
        
        $("#setting-button").click(function() {
            View._settingButtonOnClick();
        });
        
        $("#message-block").click(function() {
            View._messageDivOnClick();
        });
        
        $("h4.entry-content-title").click(function() {
            View._entryContentTitleDivOnClick();
        });
    }
}

///////////////////////////////////////////////////////////////////////////////
/**
 * an entry class of the application.
 */
var Application = function(sender) {
    $(document).ready(function() {
        sender.init(); 
    })
}

///////////////////////////////////////////////////////////////////////////////
/**
 * called the main entry.
 */
new Application(
    this
);
