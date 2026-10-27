/**
 * Auto-Updater for Web Pages
 * Automatically detects changes to HTML files or server assets and reloads the browser.
 * 
 * How to use:
 * Include this script at the bottom of your HTML before </body>:
 * <script src="/auto-updater.js"></script>
 * Or paste the code inside a <script> tag.
 */
(function() {
    'use strict';

    // Configuration
    const CONFIG = {
        intervalMs: 5000,      // Polling frequency in milliseconds (e.g. 5000 = 5 seconds)
        showNotification: true,// Show a toast alert when an update is detected
        sound: false,          // Play optional audio chime
        toastText: '🔄 Mise à jour détectée · Actualisation en cours...',
        toastTextEn: '🔄 Update detected · Refreshing...'
    };

    let initialFingerprint = null;
    let isReloading = false;

    // Fetch page fingerprint using lightweight HEAD request
    async function getFingerprint() {
        try {
            const checkUrl = window.location.pathname + '?_auto_update_ts=' + Date.now();
            const response = await fetch(checkUrl, {
                method: 'HEAD',
                cache: 'no-store',
                headers: {
                    'Cache-Control': 'no-cache, no-store, must-revalidate',
                    'Pragma': 'no-cache'
                }
            });

            if (!response.ok) return null;

            const etag = response.headers.get('ETag') || '';
            const lastMod = response.headers.get('Last-Modified') || '';
            const length = response.headers.get('Content-Length') || '';

            if (etag || lastMod || length) {
                return `${etag}|${lastMod}|${length}`;
            }

            // Fallback: If headers are stripped by local dev servers or proxies, check content length & head slice
            const full = await fetch(checkUrl, { cache: 'no-store' });
            const text = await full.text();
            return 'body_hash:' + text.length + ':' + text.slice(0, 80);
        } catch (e) {
            // Server might be temporarily reloading or offline during build
            return null;
        }
    }

    // Display a smooth notification toast
    function showUpdateToast() {
        if (!CONFIG.showNotification) return;

        let toast = document.getElementById('auto-updater-toast');
        if (!toast) {
            toast = document.createElement('div');
            toast.id = 'auto-updater-toast';
            toast.style.cssText = `
                position: fixed;
                bottom: 24px;
                left: 50%;
                transform: translateX(-50%) translateY(30px);
                background: #1E1B18;
                color: #FFFFFF;
                font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
                font-size: 13px;
                font-weight: 600;
                padding: 12px 22px;
                border-radius: 30px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.35);
                display: flex;
                align-items: center;
                gap: 10px;
                z-index: 999999;
                opacity: 0;
                transition: transform 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275), opacity 0.3s ease;
                border: 1px solid rgba(255, 255, 255, 0.15);
                pointer-events: none;
            `;

            const spinner = document.createElement('div');
            spinner.style.cssText = `
                width: 14px;
                height: 14px;
                border: 2px solid rgba(255,255,255,0.3);
                border-top-color: #FFFFFF;
                border-radius: 50%;
                animation: autoUpdaterSpin 0.8s linear infinite;
            `;

            const styleSheet = document.createElement('style');
            styleSheet.textContent = `@keyframes autoUpdaterSpin { to { transform: rotate(360deg); } }`;
            document.head.appendChild(styleSheet);

            const label = document.createElement('span');
            const isFr = (document.documentElement.lang || 'fr').startsWith('fr');
            label.textContent = isFr ? CONFIG.toastText : CONFIG.toastTextEn;

            toast.appendChild(spinner);
            toast.appendChild(label);
            document.body.appendChild(toast);
        }

        // Trigger animation
        requestAnimationFrame(() => {
            toast.style.opacity = '1';
            toast.style.transform = 'translateX(-50%) translateY(0)';
        });
    }

    // Check for updates
    async function checkForChanges() {
        if (isReloading) return;
        const currentFp = await getFingerprint();
        if (!currentFp) return;

        if (initialFingerprint === null) {
            initialFingerprint = currentFp;
            return;
        }

        if (currentFp !== initialFingerprint) {
            isReloading = true;
            console.log('[AutoUpdater] Website change detected! Reloading page...');
            showUpdateToast();
            setTimeout(() => {
                window.location.reload();
            }, 1000);
        }
    }

    // Initial fingerprint
    getFingerprint().then(fp => {
        if (fp) initialFingerprint = fp;
    });

    // Background polling loop
    setInterval(checkForChanges, CONFIG.intervalMs);

    // Instant check when user switches back to this browser tab
    document.addEventListener('visibilitychange', () => {
        if (document.visibilityState === 'visible') {
            checkForChanges();
        }
    });
    window.addEventListener('focus', checkForChanges);

})();
