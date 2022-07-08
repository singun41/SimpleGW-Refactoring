async function deleteTempDocs() {
    if(!confirm('임시문서를 삭제하시겠습니까?'))
        return;

    let docsId = document.getElementById('docsId').innerText;
    let docsType = document.getElementById('docsType').innerText;

    let response = await fetchDelete(docsType.toLowerCase() + '/temp/' + docsId);
    let result = await response.json();
    alert(result.msg);

    if(response.ok)
        location.href = 'list';
}
