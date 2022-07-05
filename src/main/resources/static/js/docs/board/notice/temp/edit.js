document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('btnTempDocsEdit').setAttribute('onclick', 'editTempNotice()');
    document.getElementById('btnTempDocsDel').setAttribute('onclick', 'deleteTempNotice()');
});

async function deleteTempNotice() {
    deleteTempDocs('notice');
}

function editTempNotice() {
    location.href = document.getElementById('docsId').innerText + '/modify';
}
