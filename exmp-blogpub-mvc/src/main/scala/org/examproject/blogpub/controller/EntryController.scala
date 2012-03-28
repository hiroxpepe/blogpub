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

package org.examproject.blogpub.controller;

import java.util.ArrayList
import java.util.List
import javax.inject.Inject
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.dozer.Mapper
import org.dozer.MappingException
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

import org.examproject.blogpub.dto.EntryDto
import org.examproject.blogpub.form.EntryForm
import org.examproject.blogpub.service.EntryService
import org.examproject.blogpub.response.Entry
import org.examproject.blogpub.response.Result

import scala.collection.JavaConversions._

/**
 * @author hiroxpepe
 */
@Controller
class EntryController {
    
    private val LOG: Logger = LoggerFactory.getLogger(classOf[EntryController])
    
    @Inject
    private val context: ApplicationContext = null

    @Inject
    private val mapper: Mapper = null

    @Inject
    private val entryService: EntryService = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    /**
     * entry form request.
     * expected HTTP request is '/entry/form.html'
     */
    @RequestMapping(
        value=Array("/entry/form"),
        method=Array(RequestMethod.GET)
    )
    def doForm(
        @CookieValue(value="__exmp_blogpub_username", defaultValue="")
        username: String,
        @CookieValue(value="__exmp_blogpub_password", defaultValue="")
        password: String,
        @CookieValue(value="__exmp_blogpub_blog", defaultValue="")
        blog: String,
        @CookieValue(value="__exmp_blogpub_url", defaultValue="")
        url: String,
        @CookieValue(value="__exmp_blogpub_scheme", defaultValue="")
        scheme: String,
        @CookieValue(value="__exmp_blogpub_feedurl", defaultValue="")
        feedUrl: String,
        @CookieValue(value="__exmp_blogpub_author", defaultValue="")
        author: String,
        model: Model
    ) = {
        LOG.info("called");
        
        // set the cookie value to the form object.
        val entryForm: EntryForm = new EntryForm()
        entryForm.setUsername(username)
        entryForm.setPassword(password)
        entryForm.setBlog(blog)
        entryForm.setUrl(url)
        entryForm.setScheme(scheme)
        entryForm.setFeedUrl(feedUrl)
        entryForm.setAuthor(author)
        model.addAttribute(entryForm)
    }
    
    /**
     * post the entry.
     * expected Ajax HTTP request is '/entry/post.html'
     */
    @RequestMapping(
        value=Array("/entry/post"),
        method=Array(RequestMethod.POST),
        headers=Array("Accept=application/json")
    )
    @ResponseBody
    def doPost(
        @RequestBody
        entryForm: EntryForm,
        model: Model
    )
    : Result = {
        LOG.info("called");
        
        // this is the return object 
        // that will be returned to the HTML page.
        var result: Result = null;
        try {
            // get the result object 
            result = context.getBean(classOf[Result]); 
            
            // map the DTO object using the form objects data.
            val entryDto: EntryDto = mapEntryFormToEntryDto(entryForm)
            
            // post the entry using the service object.
            postEntry(entryDto)
            
            // get the list of entry from the service object.
            val entryDtoList: List[EntryDto] = getEntryList(entryForm)
            
            // add to the result object.
            addResultFromEntryDtoList(entryDtoList, result)
            
            // TODO: add masseges..?
            // model.addAttribute("statusMessageKey", "entry.form.msg.success");
            
            // return the result object to HTML page. 
            // this will be converted into JSON.
            return result
            
        } catch {
            case e: Exception => {
              LOG.error(e.getMessage())

              // notify the occurrence of errors to the HTML page.
              result.setIsError(true)
              return result
            }
        }
    }

    /**
     * get the entry list.
     * expected Ajax HTTP request is '/entry/list.html'
     */
    @RequestMapping(
        value=Array("/entry/list"),
        method=Array(RequestMethod.POST),
        headers=Array("Accept=application/json")
    )
    @ResponseBody
    def doList(
        @RequestBody
        entryForm: EntryForm,
        model: Model
    )
    : Result = {
        LOG.info("called");
        
        // this is the return object
        // that will be returned to the HTML page.
        var result: Result = null;
        try {
            // get the result object
            result = context.getBean(classOf[Result])
            
            // get the list of entry from the service object.
            val entryDtoList: List[EntryDto] = getEntryList(entryForm)
            
            // add to the result object.
            addResultFromEntryDtoList(entryDtoList, result)
            
            // return the result object to HTML page. 
            // this will be converted into JSON.
            return result;
            
        } catch {
            case e: Exception => {
                LOG.error(e.getMessage())
            
                // notify the occurrence of errors to the HTML page.
                result.setIsError(true)
                return result;
            }
        }
    }
    
    /**
     * store the configuration data to the cookie.
     * expected Ajax HTTP request is '/entry/setting.html'
     */
    @RequestMapping(
        value=Array("/entry/setting"),
        method=Array(RequestMethod.POST),
        headers=Array("Accept=application/json")
    )
    def doSetting(
        @RequestBody
        entryForm: EntryForm ,
        response: HttpServletResponse 
    )
    : String = {
        LOG.info("called")
        
        try {
            // store setting param to the cookie
            storeToCookie(entryForm, response, 86400)
            
            // request a redirect to the entry/form page
            return "redirect:/entry/form.html"
            
        } catch {
            case e: Exception => {
                LOG.error(e.getMessage());
            }
        }
        return null;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // private methods
    
    /**
     * map the DTO object using the form objects data.
     */
    private def mapEntryFormToEntryDto(entryForm: EntryForm): EntryDto = {
        LOG.debug("called");
        
        val entryDto: EntryDto = context.getBean(classOf[EntryDto])
        mapper.map(entryForm, entryDto)
        return entryDto
    }

    /**
     * post the entry using the service object.
     */
    private def postEntry(entryDto: EntryDto) {
        LOG.debug("called");
        
        entryService.saveEntry(
            entryDto
        )
    }

    /**
     * get the list of entry from the service object.
     */
    private def getEntryList(entryForm: EntryForm): List[EntryDto] = {
        LOG.debug("called");
        
        val rentryList: List[EntryDto] = entryService.findAllEntry(
            entryForm.getFeedUrl()
        )
        return rentryList
    }

    /**
     * add to the result object.
     */
    private def addResultFromEntryDtoList(srcEntryList: List[EntryDto], result: Result) = {
        LOG.debug("called");
        
        val dstEntryList: List[Entry] = new ArrayList[Entry]()
        for (rentryDto: EntryDto <- srcEntryList) {
            val entry: Entry = context.getBean(classOf[Entry])
            entry.setTitle(rentryDto.getTitle())
            entry.setContent(rentryDto.getContent())
            dstEntryList.add(entry)
        }
        result.setEntryList(dstEntryList)
        result.setIsError(false)
    }
    
    /**
     * store the configuration data to the cookie.
     */
    private def storeToCookie(
        entryForm: EntryForm,
        response: HttpServletResponse,
        maxAge: Int 
    ) = {
        LOG.debug("called");
        
        var cookie: Cookie = null
        
        cookie = new Cookie("__exmp_blogpub_username", entryForm.getUsername())
        cookie.setMaxAge(maxAge)
        response.addCookie(cookie)
        
        cookie = new Cookie("__exmp_blogpub_password", entryForm.getPassword())
        cookie.setMaxAge(maxAge)
        response.addCookie(cookie)
        
        cookie = new Cookie("__exmp_blogpub_blog", entryForm.getBlog())
        cookie.setMaxAge(maxAge)
        response.addCookie(cookie)
        
        cookie = new Cookie("__exmp_blogpub_url", entryForm.getUrl())
        cookie.setMaxAge(maxAge)
        response.addCookie(cookie)
        
        cookie = new Cookie("__exmp_blogpub_scheme", entryForm.getScheme())
        cookie.setMaxAge(maxAge)
        response.addCookie(cookie)
        
        cookie = new Cookie("__exmp_blogpub_feedurl", entryForm.getFeedUrl())
        cookie.setMaxAge(maxAge)
        response.addCookie(cookie)
        
        cookie = new Cookie("__exmp_blogpub_author", entryForm.getAuthor())
        cookie.setMaxAge(maxAge)
        response.addCookie(cookie)
    }
    
}
