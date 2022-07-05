document.addEventListener('DOMContentLoaded', () => {
    buildDatatable();
});

function buildDatatable() {
    $('#datatables').DataTable({
        language: {
            paginate: {
                previous: '<i class="fa-solid fa-chevron-left"></i>',
                next: '<i class="fa-solid fa-chevron-right"></i>'
            }
        },
        order: [1, 'asc'],
        ordering: true,
        columnDefs: [
            // 0 = id hidden field.
            { targets: 0, width: '0%' },
            { targets: 1, width: '7%' },
            { targets: 2, width: '10%' },
            { targets: 3, width: '22%' },
            { targets: 4, width: '47%' },
            { targets: 5, width: '7%' },
            { targets: 6, width: '0%' },
            { targets: 7, width: '0%' },
            { targets: 8, width: '0%' },
            { targets: 9, width: '7%' }
        ],
        scrollX: true,
        scrollXInner: '100%',
        fixedHeader: true,
        scrollY: 650,
        pageLength: 100
    });
}


async function getCodes() {
    let type = document.getElementById('type').value;
    if(!type) {
        alert('Type을 선택하세요.');
        return;
    }

    let response = await fetchGet('basecode/' + type);
    let result = await response.json();

    destroyDataTable();
    removeDatalist();

    if(response.ok) {
        Array.from(result.obj).forEach(e => {
            let tr = document.createElement('tr');
            
            let id = document.createElement('td');
            let seq = document.createElement('td');
            let code = document.createElement('td');
            let value = document.createElement('td');
            let remarks = document.createElement('td');
            let enabled = document.createElement('td');
            let enabledText = document.createElement('td');
            let createdDatetime = document.createElement('td');
            let updatedDatetime = document.createElement('td');
            let edit = document.createElement('td');
            
            let i = document.createElement('i');
            i.classList.add('fa-solid', 'fa-arrow-up-right-from-square');

            let btn = document.createElement('button');
            btn.setAttribute('type', 'button');
            btn.classList.add('btn', 'btn-outline-secondary', 'btn-sm');
            btn.setAttribute('onclick', 'openEdit("' + e.id + '")');
            btn.append(i);
            edit.append(btn);

            id.innerText = e.id;
            id.classList.add('d-none');

            seq.innerText = e.seq;
            code.innerText = e.code;
            value.innerText = e.value;
            remarks.innerText = e.remarks;
            remarks.classList.add('text-start');
            
            if(e.enabled) {
                enabled.classList.add('text-success');
                enabled.innerHTML = '<i class="fa-solid fa-check"></i>';
                enabledText.innerText = '1';
            } else {
                enabled.classList.add('text-danger');
                enabled.innerHTML = '<i class="fa-solid fa-ban"></i>';
                enabledText.innerText = '0';
            }
            enabledText.classList.add('d-none');

            createdDatetime.innerText = e.createdDatetime;
            updatedDatetime.innerText = e.updatedDatetime;
            createdDatetime.classList.add('d-none');
            updatedDatetime.classList.add('d-none');

            tr.append(id, seq, code, value, remarks, enabled, enabledText, createdDatetime, updatedDatetime, edit);
            datalist.append(tr);
        });
    }
    buildDatatable();
}

function openEdit(id) {
    let option = "width=1000, height=300";
    window.open('code/' + id, '', option);
}

function openNew() {
    let type = document.getElementById('type').value;
    if(!type) {
        alert('Type을 선택하세요.');
        return;
    }
    let option = "width=1000, height=300";
    window.open('code/new/' + type, '', option);
}
