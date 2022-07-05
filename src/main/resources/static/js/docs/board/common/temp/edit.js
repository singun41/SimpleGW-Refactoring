async function deleteTempDocs(type) {
    if(!confirm('임시문서를 삭제하시겠습니까?'))
        return;
    let response = await fetchDelete(type + '/temp/' + document.getElementById('docsId').innerText);
    let result = await response.json();
    alert(result.msg);
    if(response.ok)
        location.href = 'list';
}
