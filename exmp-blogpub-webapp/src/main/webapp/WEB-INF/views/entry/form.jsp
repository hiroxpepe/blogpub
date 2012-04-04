<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div class="block">

    <div id="message-block">
    </div>
    
    <%-- in this form, the normal Http post is not used. --%>
    <%-- all of the data will request using Ajax. --%>
    <form:form id="entry-form" commandName="entryForm">
        <form:hidden path="id" />
        
        <%-- insert the entry content template. --%>
        <tiles:insertTemplate 
            template="/WEB-INF/views/entry/_entryContent.jsp"
        />
        
        <%-- insert the tab content template. --%>
        <tiles:insertTemplate 
            template="/WEB-INF/views/entry/_tabContent.jsp"
        />
        
    </form:form>
</div>