document.addEventListener('DOMContentLoaded', () => {
    dayjs.locale('ko');
    getProceedList();
});

async function getProceedList() {
    let response = await fetchGet('approval-list/proceed');
    let result = await response.json();

    destroyDataTable();
    removeDatalist();

    if(response.ok) {
        Array.from(result.obj).forEach(e => {
            let tr = document.createElement('tr');

            let id = document.createElement('td');
            let type = document.createElement('td');
            
            let title = document.createElement('td');

            let approver = document.createElement('td');
            let createdDate = document.createElement('td');

            id.textContent = e.id;
            type.textContent = e.typeTitle;

            let btn = document.createElement('button');
            let i = document.createElement('i');
            btn.setAttribute('type', 'button');
            btn.setAttribute('onclick', ('openPopup("/page/approval/' + e.type.toLowerCase() + '/' + e.id + '")'));
            btn.classList.add('btn', 'btn-outline-secondary', 'btn-sm', 'me-3');
            i.classList.add('fa-solid', 'fa-arrow-up-right-from-square');
            btn.append(i);
            title.append(btn);

            let a = document.createElement('a');
            a.setAttribute('href', '/page/approval/' + e.type.toLowerCase() + '/' + e.id);
            a.innerText = e.title;
            a.classList.add('text-decoration-none', 'text-dark');

            title.append(a)
            title.classList.add('text-start', 'align-middle');

            approver.textContent = e.approverJobTitle + ' ' + e.approverName;
            createdDate.textContent = dayjs(e.createdDate).format('YY. MM. DD.');

            tr.append(id, type, title, approver, createdDate);
            datalist.append(tr);
        });
    }
    buildDatatable();
}
