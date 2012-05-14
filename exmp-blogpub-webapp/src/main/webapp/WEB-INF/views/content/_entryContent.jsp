<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        
<div id="entry-content-wrapper" class="entry-content ui-widget-content">
    
    <h4 class="entry-content-title">
        <fmt:message key="label.entry" />
    </h4>
    
    <table class="entry-table">
        <tr>
            <td class="label-td">
                <label for="entry_title">
                    <fmt:message key="entry.form.title" />
                </label>
            </td>
            <td class="input-td" colspan=3>
                <form:input 
                    id="entry_title"
                    path="title"
                />
            </td>
        </tr>
        <tr>
            <td class="label-td">
                <label for="entry_content">
                    <fmt:message key="entry.form.content" />
                </label>
            </td>
            <td class="input-td" colspan=3>
                <form:textarea 
                    id="entry_content"
                    path="content"
                />
            </td>
        </tr>   
        <tr>
            <td class="label-td">
                <label for="entry_category">
                    <fmt:message key="entry.form.category" />
                </label>
            </td>
            <td class="input-td">
                <form:select 
                    id="entry_category"
                    path="category"
                    items="${categoryList}"
                />
            </td>
            <td class="label-td">
                <label for="entry_tags">
                    <fmt:message key="entry.form.tags" />
                </label>
            </td>
            <td class="input-td">
                <form:input 
                    id="entry_tags"
                    path="tags"
                />
            </td>
        </tr>
    </table>
    <div class="entry-command-block">
        <input 
            id="post-button"
            class="command-button"
            type="button"
            value="<fmt:message key="button.save" />"
        />
    </div>
</div>

