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
 * update the html of the profile.
 * 
 * @author hiroxpepe
 */
exmp.blogpub.functor.dhtml.ProfileUpdateClosure = {
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    execute: function(obj) {
        var username = obj.username;
        var email = obj.email;
        var hash = 0;
        if (!username) {
            username = "undefined";
        }
        if (email) {
            hash = MD5_hexhash(email);
        }
        $("#user-profile").html(               
            "<table>" +
                "<tr>" +
                    "<td>" +
                        "<img src='http://2.gravatar.com/avatar/" +
                            hash + "' width='48' height='48' border='0'>" +
                    "</td>" +
                    "<td>" +
                        "<div class='profileName'><b>" + username + "</b></div>" +
                    "</td>" +
                "</tr>" +
            "</table>"
        );
    }
}
