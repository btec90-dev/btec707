// Vercel KV REST API Service for Notes
// Uses Vercel KV REST API environment variables (VITE_KV_REST_API_URL and VITE_KV_REST_API_TOKEN)

export async function fetchNotesFromKV() {
  try {
    const kvUrl = import.meta.env.VITE_KV_REST_API_URL;
    const kvToken = import.meta.env.VITE_KV_REST_API_TOKEN;

    if (kvUrl && kvToken) {
      const res = await fetch(`${kvUrl}/get/fatou_ivf_notes`, {
        headers: {
          Authorization: `Bearer ${kvToken}`
        }
      });
      const data = await res.json();
      if (data && data.result) {
        const parsed = typeof data.result === 'string' ? JSON.parse(data.result) : data.result;
        if (Array.isArray(parsed)) return parsed;
      }
    } else {
      // Fallback to localStorage
      const local = localStorage.getItem('fatou_ivf_notes');
      if (local) return JSON.parse(local);
    }
  } catch (err) {
    console.error("Error fetching notes from Vercel KV:", err);
  }
  return null;
}

export async function saveNotesToKV(notes) {
  try {
    const kvUrl = import.meta.env.VITE_KV_REST_API_URL;
    const kvToken = import.meta.env.VITE_KV_REST_API_TOKEN;

    // Always save to localStorage backup
    localStorage.setItem('fatou_ivf_notes', JSON.stringify(notes));

    if (kvUrl && kvToken) {
      await fetch(`${kvUrl}/set/fat_ivf_notes`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${kvToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(notes)
      });
    }
  } catch (err) {
    console.error("Error saving notes to Vercel KV:", err);
  }
}
