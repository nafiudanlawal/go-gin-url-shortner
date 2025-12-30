const API_BASE_URL = "http://localhost:8080"
function validateUrl(url) {
    const pattern = new RegExp(
        '^([a-zA-Z]+:\\/\\/)?' + // protocol
        '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // domain name
        '((\\d{1,3}\\.){3}\\d{1,3}))' + // OR IP (v4) address
        '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // port and path
        '(\\?[;&a-z\\d%_.~+=-]*)?' + // query string
        '(\\#[-a-z\\d_]*)?$', // fragment locator
        'i'
    );
    return pattern.test(url);
};

function handleShowExamplesToggle() {
    console.log("clicked");
    const exampleContainer = document.getElementById("examplesDiv");
    exampleContainer.classList.toggle("hidden")
}

function handleInputKeyup() {
    const urlInput = document.getElementById("urlInput");
    if (!urlInput.value.startsWith('http')) {
        urlInput.value = 'https://' + urlInput.value;
    }
};

function copyLinkToClipBoard() {
    const showCodeUrlLink = document.getElementById("showCodeUrl");
    navigator.clipboard.writeText(showCodeUrlLink.href);
    alert("copied to clipboard");
}

const form = document.getElementById("urlForm");
form.addEventListener("submit", async (event) => {
    event.preventDefault();
    const button = document.getElementById("submitBtn");
    const formContainer = document.getElementById("formContainer");
    const urlInput = document.getElementById("urlInput");
    const errorText = document.getElementById("errorText");


    if (!validateUrl(urlInput.value)) {
        errorText.innerHTML = 'Invalid url provided.';
        errorText.classList.remove("hidden");
        return;
    }

    button.setAttribute("disabled", "");
    errorText.classList.add("hidden");
    const res = await fetch(`${API_BASE_URL}/shorten`, {
        method: 'POST',
        headers: {
            'content-type': 'application/json'
        },
        body: JSON.stringify({url: urlInput.value})
    });

    const status = res.ok;
    const data = await res.json();
    const showCode = document.getElementById("showCode");
    button.removeAttribute("disabled");
    if (status) {
        const showCodeUrlLink = document.getElementById("showCodeUrl");
        formContainer.classList.add("hidden");
        showCode.classList.remove("hidden");
        showCodeUrlLink.innerHTML = `${API_BASE_URL}/${data["shortCode"]}`;
        showCodeUrlLink.href = `${API_BASE_URL}/${data["shortCode"]}`;

    } else {
        showCode.classList.add("hidden");
        errorText.innerHTML = data?.message ?? 'Failed to create a short url.';
        errorText.classList.remove("hidden");
    }
});