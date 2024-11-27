export function setCookie(cookieName, cookieValue) {
    document.cookie = `${cookieName}=${cookieValue}; path=/; max-age=3600`
}

export function getCookieValue(name) {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return match ? match[2] : null;
}