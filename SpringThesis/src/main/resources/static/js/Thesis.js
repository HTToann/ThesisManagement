function openAddThesisModal() {
    const form = document.getElementById("ThesisForm");
    form.reset();
    form.setAttribute("data-mode", "add");
    new bootstrap.Modal(document.getElementById("ThesisModal")).show();
}

function editThesis(id, title, description, year, semester, boardId) {
    const form = document.getElementById("ThesisForm");
    form.setAttribute("data-mode", "edit");
    form.thesisId.value = id;
    form.title.value = title;
    form.description.value = description;
    form.year.value = year;
    form.semester.value = semester;
    form.boardId.value = boardId;

    new bootstrap.Modal(document.getElementById("ThesisModal")).show();
}

document.getElementById("ThesisForm").addEventListener("submit", async function (e) {
    e.preventDefault();
    const form = e.target;
    const payload = {
        title: form.title.value,
        description: form.description.value,
        year: form.year.value,
        semester: form.semester.value,
        boardId: form.boardId.value

    };

    let url = "/SpringThesis/api/secure/thesis";
    let method = "POST";

    if (form.getAttribute("data-mode") === "edit") {
        url += "/" + form.thesisId.value;
        method = "PUT";
    }

    const res = await fetch(url, {
        method,
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(payload)
    });

    if (res.ok) {
        alert("Lưu thành công!");
        location.reload();
    } else {
        const err = await res.json().catch(() => ({}));
        alert("Lỗi: " + (err.error || "Không xác định"));
    }
});

function deleteThesis(id) {
    if (!confirm("Bạn có chắc chắn muốn xoá khóa luận này?"))
        return;
    fetch(`/SpringThesis/api/secure/thesis/${id}`, {
        method: "DELETE"
    }).then(res => {
        if (res.ok) {
            alert("Đã xoá!");
            location.reload();
        } else {
            alert("Không thể xoá khóa luận.");
        }
    }).catch(() => alert("Lỗi kết nối khi xoá."));
}
