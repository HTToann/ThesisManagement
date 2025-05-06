function openAddBoardMemberModal() {
    const form = document.getElementById("BoardMemberForm");
    form.reset();
    form.setAttribute("data-mode", "add");
    form.boardId.disabled = false;
    form.lecturerId.disabled = false;
    new bootstrap.Modal(document.getElementById("BoardMemberModal")).show();
}
function handleEditBoardMember(button) {
    const boardId = button.getAttribute("data-board-id");
    const lecturerId = button.getAttribute("data-lecturer-id");
    const roleInBoard = button.getAttribute("data-role");

    editBoardMember(boardId, lecturerId, roleInBoard);
}
function editBoardMember(boardId, lecturerId, currentRole) {
    const form = document.getElementById("BoardMemberForm");
    form.setAttribute("data-mode", "edit");

    form.boardId.value = boardId;
    form.lecturerId.value = lecturerId;
    form.roleInBoard.value = currentRole;

    form.boardId.disabled = true;
    form.lecturerId.disabled = true;

    new bootstrap.Modal(document.getElementById("BoardMemberModal")).show();
}

document.getElementById("BoardMemberForm").addEventListener("submit", async function (e) {
    e.preventDefault();
    const form = e.target;
    const mode = form.getAttribute("data-mode");
    const boardId = form.boardId.value;
    const lecturerId = form.lecturerId.value;
    const roleInBoard = form.roleInBoard.value;

    const payload = { roleInBoard };
    let url = "/SpringThesis/api/secure/board-members";
    let method = "POST";
    let successMsg = "Thêm thành viên thành công!";

    if (mode === "edit") {
        url += `/${boardId}/${lecturerId}`;
        method = "PATCH";
        successMsg = "Cập nhật vai trò thành công!";
    } else {
        Object.assign(payload, { boardId, lecturerId });
    }

    try {
        const response = await fetch(url, {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert(successMsg);
            location.reload();
        } else {
            const err = await response.json().catch(() => ({}));
            alert("Lỗi: " + (err.error || "Không xác định"));
        }
    } catch (error) {
        alert("Lỗi kết nối đến server!");
    }
});

function deleteBoardMember(boardId, lecturerId) {
    if (!confirm("Xác nhận xoá thành viên khỏi hội đồng?")) return;

    fetch(`/SpringThesis/api/secure/board-members/${boardId}/${lecturerId}`, {
        method: "DELETE"
    })
    .then(res => {
        if (res.ok) {
            alert("Đã xoá thành viên!");
            location.reload();
        } else {
            alert("Lỗi khi xoá thành viên!");
        }
    })
    .catch(() => alert("Lỗi kết nối khi xoá thành viên!"));
}
