<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="block menu-content">
    <div class="menu-bar">
        <a href="<c:url value="/" />"><fmt:message key="index.title" /></a>
        <a href="<c:url value="/entry/form.html" />"><fmt:message key="button.entry" /></a>
    </div>
</div>