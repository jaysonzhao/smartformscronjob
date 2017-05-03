<!doctype html>
<title>CodeMirror: HTML mixed mode</title>
<meta charset="utf-8"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/codeMirror/lib/codemirror.css">
<script src="${pageContext.request.contextPath}/scripts/codeMirror/lib/codemirror.js"></script>
<script src="${pageContext.request.contextPath}/scripts/codeMirror/addon/selection/selection-pointer.js"></script>
<script src="${pageContext.request.contextPath}/scripts/codeMirror/mode/xml/xml.js"></script>
<script src="${pageContext.request.contextPath}/scripts/codeMirror/mode/javascript/javascript.js"></script>
<script src="${pageContext.request.contextPath}/scripts/codeMirror/mode/css/css.js"></script>
<script src="${pageContext.request.contextPath}/scripts/codeMirror/mode/vbscript/vbscript.js"></script>
<script src="${pageContext.request.contextPath}/scripts/codeMirror/mode/htmlmixed/htmlmixed.js"></script>
<style>.CodeMirror {border: 1px solid black;}</style>
<textarea id="code" name="code">


</textarea>
    <script>
      var mixedMode = {
        name: "htmlmixed",
        scriptTypes: [{matches: /\/x-handlebars-template|\/x-mustache/i,
                       mode: null},
                      {matches: /(text|application)\/(x-)?vb(a|script)/i,
                       mode: "vbscript"}]
      };
      var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
        mode: mixedMode,
          lineNumbers: true,
        selectionPointer: true
      });
   editor.setValue("&lt;script type=\"text/javascript\"&rt;alert(1);&lt;/script&rt;");
    </script>