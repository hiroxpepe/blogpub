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
 * send HTTP request for the setting.
 * 
 * @author hiroxpepe
 */
exmp.blogpub.functor.request.SettingClosure = {
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    execute: function(obj) {
        
        var waitingMessageClosure = exmp.blogpub.functor.dhtml.WaitingMessageClosure;
        
        var successMessageClosure = exmp.blogpub.functor.dhtml.SuccessMessageClosure;
        
        var errorMessageClosure = exmp.blogpub.functor.dhtml.ErrorMessageClosure;
        
        // show the waiting message.
        waitingMessageClosure.execute({
            message: "please wait..."
        });
        
        // create an ajax object.
        new $.ajax({
            url: "setting.html",
            type: "POST",
            data: obj,
            dataType: "json",
            contentType: "application/json;charset=UTF-8",
            
            // callback function of the success.
            success: function(data, dataType) {
            },
            
            // callback function of the error.
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                
                // because this Ajax requests are redirected as normal.
                if (XMLHttpRequest.status == 200) {
                    // show the success message.
                    successMessageClosure.execute({
                        message: "complete."
                    });
                    return;
                }
                
                // show the error message.
                errorMessageClosure.execute({
                    message: "httprequest error occurred.."
                });
            }
        });
    }
}
