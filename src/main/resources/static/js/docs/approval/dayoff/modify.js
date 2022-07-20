window.addEventListener('DOMContentLoaded', () => {
    getDetails(docsId);
});

async function update() {
    let arrDayoffCode = [];
    Array.from(document.getElementsByClassName('dayoff-code')).forEach(e => { arrDayoffCode.push(e.value === '' ? null : e.value); });

    let arrDateFrom = [];
    let arrDateTo = [];
    Array.from(document.getElementsByClassName('input-date')).forEach(e => {
        let dt = e.value.replaceAll(' ', '').split('~');
        let dtFrom = dt[0].substr(0, 10).replaceAll('.', '-');
        let dtTo;

        if(dt.length === 1)
            dtTo = dtFrom;
        else
            dtTo = dt[1].substr(0, 10).replaceAll('.', '-');
        
        arrDateFrom.push(dtFrom);
        arrDateTo.push(dtTo);
    });

    let arrDetail = [];
    for(let i=0; i<arrDayoffCode.length; i++) {
        let detailData = {
            code: arrDayoffCode[i],
            dateFrom: arrDateFrom[i],
            dateTo: arrDateTo[i]
        };
        arrDetail.push(detailData);
    }
    
    let params = {
        content: document.getElementById('content').value,
        details: arrDetail
    };

    let docsId = await updateApprovalDocs(params);
    if(docsId) {
        saveComplete = true;
        location.href = `/page/approval/${docsType.toLowerCase()}/${docsId}`;   // common/modify.js와 url이 다르다.
    }
}
