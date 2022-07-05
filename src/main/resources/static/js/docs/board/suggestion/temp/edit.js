document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('btnTempDocsEdit').setAttribute('onclick', 'editTempSuggestion()');
    document.getElementById('btnTempDocsDel').setAttribute('onclick', 'deleteTempSuggestion()');
});

async function deleteTempSuggestion() {
    deleteTempDocs('suggestion');
}

function editTempSuggestion() {
    location.href = document.getElementById('docsId').innerText + '/modify';
}
