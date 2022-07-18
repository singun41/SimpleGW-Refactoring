window.addEventListener('DOMContentLoaded', () => {
    // copyCheck();
});

async function save() {
    let arrDayoffCode = [];
    Array.from(document.getElementsByClassName('dayoff-code')).forEach(e => { arrDayoffCode.push(e.value === '' ? null : e.value); });

    let arrDateStart = [];
    let arrDateEnd = [];
    Array.from(document.getElementsByClassName('input-date')).forEach(e => {
        let dt = e.value.replaceAll(' ', '').split('~');
        let dtFrom = dt[0].substr(0, 10).replaceAll('.', '-');
        let dtTo;

        if(dt.length === 1)
            dtTo = dtFrom;
        else
            dtTo = dt[1].substr(0, 10).replaceAll('.', '-');
        
        arrDateStart.push(dtFrom);
        arrDateEnd.push(dtTo);
    });

    let arrDetail = [];
    for(let i=0; i<arrDayoffCode.length; i++) {
        let detailData = {
            code: arrDayoffCode[i],
            dateStart: arrDateStart[i],
            dateEnd: arrDateEnd[i]
        };
        arrDetail.push(detailData);
    }
    
    let params = {
        content: document.getElementById('content').value,
        details: arrDetail
    };

    let docsId = await saveApprovalDocs(params);
    if(docsId)
        location.href = `/page/approval/${docsType.toLowerCase()}/${docsId}`;
}

async function saveTemp() {

}
