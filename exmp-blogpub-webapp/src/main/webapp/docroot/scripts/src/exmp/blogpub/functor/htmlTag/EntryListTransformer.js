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
 * this class is a transformer that JSON data get by
 * Ajax HTTP requests and convert to HTML tables.
 * 
 * @author hiroxpepe
 */
exmp.blogpub.functor.htmltag.EntryListTransformer = {
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    transform: function(obj) {
        var table = "<table class='entry-list-table'>";
        for (var i = 0; i < obj.entryList.length; i++) {
            
            var title = $.erasureHTML(
                obj.entryList[i].title
            );
            
            var content = $.erasureHTML(
                obj.entryList[i].content
            );
            
            table +=
                "<tr>" +
                    "<td class='entry-list-td' >" +
                        title + " " + content +
                    "</td>" +
                "</tr>";
        }
        table += "</table>";
        return table;
    }
}
