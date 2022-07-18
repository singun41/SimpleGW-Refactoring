document.addEventListener('DOMContentLoaded', () => {
    let btnDocsCopy = document.getElementById('btnDocsCopy');
    if(btnDocsCopy)
        btnDocsCopy.setAttribute('onclick', 'copyDocs()');
});

function copyDocs() {
    let arrDetails = [];
    let rowsCnt = document.getElementsByClassName('detail-rows').length;

    for(let i=0; i<rowsCnt; i++) {
        let dt = document.getElementsByClassName('detail-date')[i].textContent;
        let dtFrom = dt.substring(0, 11);
        let dtTo = dt.length > 11 ? dt.substring(15, 26) : dt.substring(0, 11);

        let details = {
            code: document.getElementsByClassName('detail-code')[i].innerHTML,
            dateStart: dtFrom,
            dateEnd: dtTo
        };

        console.log(details);
        arrDetails.push(details);
    }

    let docs = {
        'title': document.getElementById('docsTitle').innerHTML,
        'content': document.getElementById('docsContent').value,
        'details': arrDetails
    };
    
    console.log(JSON.stringify(docs));
    return;

    localStorage.setItem('docs', JSON.stringify(docs));
    location.href = 'write';
}
