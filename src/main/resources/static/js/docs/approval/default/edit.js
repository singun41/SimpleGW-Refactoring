document.addEventListener('DOMContentLoaded', () => {
    let btnDocsEdit = document.getElementById('btnDocsEdit');
    let btnDocsDel = document.getElementById('btnDocsDel');
    
    if(btnDocsEdit)
        btnDocsEdit.setAttribute('onclick', 'editDocs()');
    
    if(btnDocsDel)
        btnDocsDel.setAttribute('onclick', 'deleteDocs("default")');
});
