<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div class="block sidebar-content">
    <%-- insert the profile content --%>
    <tiles:insertTemplate 
        template="/WEB-INF/views/_profile.jsp"
    />
    <a href="<c:url value="/entry/form.html" />">
        <fmt:message key="button.entry" />
    </a>
</div>