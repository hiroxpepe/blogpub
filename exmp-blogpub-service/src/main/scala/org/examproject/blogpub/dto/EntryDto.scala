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

package org.examproject.blogpub.dto

import scala.reflect.BeanProperty

/**
 * @author hiroxpepe
 */
trait EntryDto {
    
    @BeanProperty
    var id: Int
    
    @BeanProperty
    var url: String
    
    @BeanProperty
    var username: String
    
    @BeanProperty
    var password: String
    
    @BeanProperty
    var blog: String
    
    @BeanProperty
    var scheme: String
    
    @BeanProperty
    var author: String
    
    @BeanProperty
    var title: String
    
    @BeanProperty
    var content: String
    
    @BeanProperty
    var category: String
    
    @BeanProperty 
    var tags: String
    
}
