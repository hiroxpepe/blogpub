/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

///////////////////////////////////////////////////////////////////////////////
/**
 * a functor class of the application.
 * send HTTP request for get the entry list.
 * 
 * @author hiroxpepe
 */
exmp.blogpub.functor.request.EntryListClosure = {
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    execute: function(obj) {
        
        var listWaitingClosure = exmp.blogpub.functor.dhtml.ListWaitingClosure;
        
        var errorMessageClosure = exmp.blogpub.functor.dhtml.ErrorMessageClosure;
        
        var entryListUpdateClosure = exmp.blogpub.functor.dhtml.EntryListUpdateClosure;
        
        // show the waiting message.
        listWaitingClosure.execute(
            null
        );
            
        // create an ajax object.
        new $.ajax({
            url: "list.html",
            type: "POST",
            data: obj,
            dataType: "json",
            contentType: "application/json;charset=UTF-8",
            
            // callback function of the success.
            success: function(data, dataType) {
                
                // if get a error from the response.
                if (data.isError) {
                    // show the error message.
                    errorMessageClosure.execute({
                        message: "application error occurred.."
                    });
                    $("#entry-list-block").html("");
                    return;
                }
                
                // update the HTML table of the entry list.
                entryListUpdateClosure.execute(
                    data
                );
            },
            
            // callback function of the error.
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                
                // show the error message.
                errorMessageClosure.execute({
                    message: "httprequest error occurred.."
                });
                $("#entry-list-block").html("");
            }
        });
    }
}
