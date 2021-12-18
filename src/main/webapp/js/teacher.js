const teacherBaseUrl = "http://localhost:8080/teachers";

function getTeachers() {
    $.ajax({
        method: "GET",
        headers: {'Content-Type': 'application/json'},
        url: teacherBaseUrl,
    }).done((response) => {
        renderTeachers(response);
    });
}

function renderTeachers(teachers = []) {
    const teacherSelect = $("#teacher-select");
    teachers.forEach((teacher) =>
        teacherSelect.append(
            `<option value="${teacher.id}">${teacher.firstName} ${teacher.lastName}</option>`
        )
    );
}

function createTeacher() {
    $.ajax({
        url: teacherBaseUrl,
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        data: JSON.stringify(getTeacherFormFields()),
        dataType: 'json',
        statusCode: {
            200: handle200,
            201: handle201,
            404: handle404
        }
    })
        .done(() => {
            window.location.replace("./teachers.html");
        })
        .fail((response) => {
        });
}

function editTeacher() {
    $.ajax({
        url: `${teacherBaseUrl}`,
        method: "PUT",
        headers: {'Content-Type': 'application/json'},
        data: JSON.stringify(getTeacherFormFields()),
        dataType: 'json',
        statusCode: {
            200: handle200,
            201: handle201,
            404: handle404
        }
    })
        .done(() => {
            window.location.replace("./teachers.html");
        })
        .fail((response) => {
        });
}

function deleteTeacher(id) {
    $.ajax({
        url: `${teacherBaseUrl}/${id}`,
        method: "DELETE",
        success: function () {
            renderTeachersList();
        },
    });
}

function renderTeachersList() {
    $.ajax({
        method: "GET",
        headers: {'Content-Type': 'application/json'},
        url: teacherBaseUrl,
    })
        .done((response) => {
            renderTeacherList(response);
            $("#teacher-list").show();
        })
        .fail((response) => {
            const $errorAlert = $("#error-alert");
            $errorAlert.text(response.message);
            $errorAlert.removeClass("d-none");
        })
        .always(() => {
            $("#teacher-loading").addClass("d-none").removeClass("d-flex");
        });

    $("#teacher-list").hide();
    $("#error-alert").addClass("d-none");
    $("#teacher-loading").addClass("d-flex").removeClass("d-none");
}

function renderTeacherList(teachers = []) {
    $teacherList = $("#teacher-list");
    $teacherList.empty();

    teachers.forEach((teacher) => {
        const $template = getTeacherTemplate(teacher);
        $teacherList.append($template);
    });
}

function getTeacherTemplate(teacher) {
    const templateSelector = `#teacher-list-template`;
    const $template = $($(templateSelector).html());
    $template
        .find(".teacher-name")
        .text(`${teacher.firstName} ${teacher.lastName}`);

    $template
        .find(".teacher-availability")
        .text(teacher.available ? "Available" : "Unavailable");
    $template.find("#delete-teacher").click(() => deleteTeacher(teacher.id));
    $template
        .find("#edit-teacher")
        .click(() =>
            window.location.replace(`./teacher-edit.html?id=${teacher.id}`)
        );
    return $template;
}

function getTeacherFormFields() {
    const id = $("#teacher-id").val();
    const firstName = $("#firstName").val();
    const lastName = $("#lastName").val();
    const available = $("#availabilityRadios").val() === 1;

    return {"id": id, "firstName": firstName, "lastName": lastName, "available": available};
}

function fillFormForTeacher(id) {
    $.ajax({
        method: "GET",
        headers: {'Content-Type': 'application/json'},
        url: `${teacherBaseUrl}/${id}`,
    }).done((response) => {
        $("#teacher-id").val(response.id);
        $("#firstName").val(response.firstName);
        $("#lastName").val(response.lastName);
        $(
            `input[name=availabilityRadios][value=${response.available ? 1 : 0}]`
        ).prop("checked", true);
    });
}

const handle200 = function (data, textStatus, jqXHR) {
    window.location.replace("./teachers.html");
};

const handle201 = function (data, textStatus, jqXHR) {
    window.location.replace("./teachers.html");
};

const handle404 = function (jqXHR, textStatus, errorThrown) {
    alert(errorThrown);
};
