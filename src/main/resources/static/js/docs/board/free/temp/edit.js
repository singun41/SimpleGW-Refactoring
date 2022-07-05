document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('btnTempDocsEdit').setAttribute('onclick', 'editTempFreeboard()');
    document.getElementById('btnTempDocsDel').setAttribute('onclick', 'deleteTempFreeboard()');
});

async function deleteTempFreeboard() {
    deleteTempDocs('freeboard');
}

function editTempFreeboard() {
    location.href = document.getElementById('docsId').innerText + '/modify';
}
