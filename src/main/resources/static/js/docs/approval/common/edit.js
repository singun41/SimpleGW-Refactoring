function editDocs() {
    location.href = document.getElementById('docsId').innerText + '/modify';
}

async function deleteDocs(type) {
    if(!confirm('문서를 삭제하시겠습니까?'))
        return;
    let response = await fetchDelete(type + '/' + document.getElementById('docsId').innerText);
    let result = await response.json();
    alert(result.msg);
    if(response.ok)
        location.href = 'list';
}
