function openAddCriteriaModal() {
    const form = document.getElementById('CriteriaForm');
    form.reset();
    form.criteriaId.value = ""; // Để phân biệt thêm mới

    const modal = new bootstrap.Modal(document.getElementById('CriteriaModal'));
    modal.show();
}

document.getElementById('CriteriaForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const form = document.getElementById('CriteriaForm');
    const criteriaId = form.criteriaId.value;

    const payload = {
        name: form.name.value,
        max_score: form.maxScore.value.toString()
    };

    let url = '/SpringThesis/api/secure/criteria';
    let method = 'POST';

    if (criteriaId) {
        url += `/${criteriaId}`;
        method = 'PUT';
    }

    const response = await fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    });

    if (response.ok) {
        alert(criteriaId ? "Cập nhật tiêu chí thành công!" : "Thêm tiêu chí thành công!");
        location.reload();
    } else {
        const err = await response.json();
        alert("Lỗi: " + (err.error || "Không xác định"));
    }
});

function editCriteriaFromButton(button) {
    const criteria = {
        criteriaId: button.getAttribute('data-id'),
        name: button.getAttribute('data-name'),
        maxScore: button.getAttribute('data-maxscore')
    };

    editCriteria(criteria);
}

function editCriteria(criteria) {
    const form = document.getElementById('CriteriaForm');
    form.criteriaId.value = criteria.criteriaId;
    form.name.value = criteria.name;
    form.maxScore.value = criteria.maxScore;

    new bootstrap.Modal(document.getElementById('CriteriaModal')).show();
}

function deleteCriteria(id) {
    if (!confirm("Xác nhận xoá tiêu chí?"))
        return;

    fetch(`/SpringThesis/api/secure/criteria/${id}`, {
        method: 'DELETE'
    }).then(res => {
        if (res.ok) {
            alert("Đã xoá tiêu chí!");
            location.reload();
        } else {
            alert("Lỗi khi xoá tiêu chí!");
        }
    });
}
