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

package org.examproject.blogpub.controller

import java.util.ArrayList
import java.util.List
import javax.inject.Inject
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.dozer.Mapper
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody

import org.examproject.blogpub.dto.EntryDto
import org.examproject.blogpub.form.EntryForm
import org.examproject.blogpub.service.EntryService
import org.examproject.blogpub.response.AjaxResponse
import org.examproject.blogpub.response.Entry

import scala.collection.JavaConversions._

/**
 * the entry controller class of the application.
 *
 * @author hiroxpepe
 */
@Controller
class EntryController {
    
    private val LOG: Logger = LoggerFactory.getLogger(
        classOf[EntryController]
    )
    
    @Inject
    private val context: ApplicationContext = null

    @Inject
    private val mapper: Mapper = null

    @Inject
    private val entryService: EntryService = null
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    ///////////////////////////////////////////////////////////////////////////
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
        LOG.info("called")
             
        // create a form-object.
        val entryForm: EntryForm = context.getBean(
            classOf[EntryForm]
        )
        
        // set the cookie value to the form-object.
        entryForm.setUsername(username)
        entryForm.setPassword(password)
        entryForm.setBlog(blog)
        entryForm.setUrl(url)
        entryForm.setScheme(scheme)
        entryForm.setFeedUrl(feedUrl)
        entryForm.setAuthor(author)
        
        // set the form-object to the model. 
        model.addAttribute(
            entryForm
        )
    }
    
    ///////////////////////////////////////////////////////////////////////////
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
    : AjaxResponse = {
        LOG.info("called")
        
        // the response-object will be returned to the html page.
        
        // create a response-object.
        val response: AjaxResponse = context.getBean(
            classOf[AjaxResponse]
        )
            
        // get the mapped dto-object using the form-object data.
        val entryDto: EntryDto = getMappedEntry(
            entryForm
        )
            
        // post the entry dto-object using the service-object.
        postEntry(
            entryDto
        )
            
        // get the list of dto-object from the service-object.
        val entryDtoList: List[EntryDto] = getEntryList(
            entryForm
        )
            
        // add to the response-object.
        addToResponse(
            entryDtoList,
            response
        )
            
        // return the response-object to html page. 
        // this will be converted into json.
        return response
    }

    ///////////////////////////////////////////////////////////////////////////
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
    : AjaxResponse = {
        LOG.info("called")
        
        // the response-object will be returned to the html page.
        
        // create a response-object.
        val response: AjaxResponse = context.getBean(
            classOf[AjaxResponse]
        )
            
        // get the mapped dto-object using the form-object data.
        val entryDtoList: List[EntryDto] = getEntryList(
            entryForm
        )
            
        // add to the response-object.
        addToResponse(
            entryDtoList,
            response
        )
            
        // return the response-object to html page.
        // this will be converted into json.
        return response;
    }
    
    ///////////////////////////////////////////////////////////////////////////
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

        // store the setting param to the cookie.
        storeToCookie(
            entryForm,
            response,
            604800
        )

        // redirect the request to the 'entry/form' page.
        return "redirect:/entry/form.html"
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * if an error is occured, this method will be called.
     */
    @ExceptionHandler
    @ResponseBody
    def handleException(
        e: Exception
    )
    : AjaxResponse = {
        LOG.info("called")
        
        LOG.error(e.getMessage())
        
        // create a response-object.
        val response: AjaxResponse = context.getBean(
            classOf[AjaxResponse]
        )
        
        // notify the occurrence of errors to the html page.
        response.setIsError(true)
        return response;
    } 
    
    ///////////////////////////////////////////////////////////////////////////
    // private methods
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * map the dto-object using the form object-data.
     */
    private def getMappedEntry(
        entryForm: EntryForm
    )
    : EntryDto = {
        LOG.debug("called");
        
        // create a dto-object.
        val entryDto: EntryDto = context.getBean(
            classOf[EntryDto]
        )
        
        // map the form-object to the dto-object.
        mapper.map(
            entryForm,
            entryDto
        )
        
        return entryDto
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * post the dto-object of the entry using the service-object.
     */
    private def postEntry(
        entryDto: EntryDto
    ) = {
        LOG.debug("called");
        
        // save the dto-object.
        entryService.saveEntry(
            entryDto
        )
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * get the list of entry from the service-object.
     */
    private def getEntryList(
        entryForm: EntryForm
    )
    : List[EntryDto] = {
        LOG.debug("called");
        
        // get the dto-object list from service-object.
        val rentryList: List[EntryDto] = entryService.findAllEntry(
            entryForm.getFeedUrl()
        )
        
        return rentryList
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * add to the response-object.
     */
    private def addToResponse(
        srcEntryList: List[EntryDto],
        response: AjaxResponse
    ) = {
        LOG.debug("called");
        
        // create a list of entry object, 
        // in order to send to the html page.
        val dstEntryList: List[Entry] = new ArrayList[Entry]()
        
        // process the entry object of all of the list.
        for (rentryDto: EntryDto <- srcEntryList) {
            
            // create a object to send to the html page.
            val entry: Entry = context.getBean(
                classOf[Entry]
            )
            
            // set the value to the object.
            entry.setTitle(
                rentryDto.getTitle()
            )
            entry.setContent(
                rentryDto.getContent()
            )
            
            // add the object to the object list.
            dstEntryList.add(
                entry
            )
        }
        
        // set the object list to response-object.
        response.setEntryList(
            dstEntryList
        )
        
        // set the error status.
        response.setIsError(
            false
        )
    }
    
    ///////////////////////////////////////////////////////////////////////////
    /**
     * store the configuration data to the cookie.
     */
    private def storeToCookie(
        entryForm: EntryForm,
        response: HttpServletResponse,
        maxAge: Int 
    ) = {
        LOG.debug("called");
        
        val username = new Cookie(
            "__exmp_blogpub_username",
            entryForm.getUsername()
        )
        username.setMaxAge(maxAge)
        response.addCookie(username)
        
        val password = new Cookie(
            "__exmp_blogpub_password",
            entryForm.getPassword()
        )
        password.setMaxAge(maxAge)
        response.addCookie(password)
        
        val blog = new Cookie(
            "__exmp_blogpub_blog",
            entryForm.getBlog()
        )
        blog.setMaxAge(maxAge)
        response.addCookie(blog)
        
        val url = new Cookie(
            "__exmp_blogpub_url",
            entryForm.getUrl()
        )
        url.setMaxAge(maxAge)
        response.addCookie(url)
        
        val scheme = new Cookie(
            "__exmp_blogpub_scheme",
            entryForm.getScheme()
        )
        scheme.setMaxAge(maxAge)
        response.addCookie(scheme)
        
        val feedurl = new Cookie(
            "__exmp_blogpub_feedurl",
            entryForm.getFeedUrl()
        )
        feedurl.setMaxAge(maxAge)
        response.addCookie(feedurl)
        
        val author = new Cookie(
            "__exmp_blogpub_author",
            entryForm.getAuthor()
        )
        author.setMaxAge(maxAge)
        response.addCookie(author)
    }
    
}
