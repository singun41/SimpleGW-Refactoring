document.addEventListener('DOMContentLoaded', () => {
    let btnDocsCopy = document.getElementById('btnDocsCopy');
    let btnDocsEdit = document.getElementById('btnDocsEdit');
    let btnDocsDel = document.getElementById('btnDocsDel');

    if(btnDocsCopy)
        btnDocsCopy.setAttribute('onclick', 'copyDocs()');
    
    if(btnDocsEdit)
        btnDocsEdit.setAttribute('onclick', 'editDocs()');
    
    if(btnDocsDel)
        btnDocsDel.setAttribute('onclick', 'deleteDocs("suggestion")');
});
