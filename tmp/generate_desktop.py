import os, base64

svgs = {}
for fn in ['journal_pgta.svg', 'journal_sis.svg', 'journal_transfer.svg', 'journal_betahcg.svg', 'journal_ultrasound.svg']:
    p = os.path.join('public/assets', fn)
    if os.path.exists(p):
        with open(p, 'rb') as f:
            svgs[fn] = 'data:image/svg+xml;base64,' + base64.b64encode(f.read()).decode('utf-8')
    else:
        svgs[fn] = ''

html_content = f'''<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mon Parcours FIV · Fatou ♥ (Desktop Brown Edition)</title>
    <style>
        :root {{
            --brown-primary: #5C3B24;
            --brown-secondary: #8D6648;
            --brown-dark: #382214;
            --brown-soft: #F4EBD8;
            --cream-bg: #FAF6F0;
            --card-bg: #FFFFFF;
            --ink: #2B2320;
            --muted: #6E6058;
            --line: #E2D3C4;
            --line-subtle: #EDE4DA;
            --green: #2B7853;
            --green-bg: #EAF4ED;
            --green-line: #BFE0CB;
            --gold-gradient: linear-gradient(135deg, #7A4F31 0%, #5C3B24 100%);
            --shadow-sm: 0 2px 8px rgba(92, 59, 36, 0.05);
            --shadow-md: 0 6px 20px rgba(92, 59, 36, 0.08);
            --shadow-lg: 0 12px 36px rgba(92, 59, 36, 0.12);
        }}

        * {{
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            -webkit-tap-highlight-color: transparent;
        }}

        body {{
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
            background-color: var(--cream-bg);
            color: var(--ink);
            line-height: 1.5;
            padding: 24px 16px;
        }}

        .desktop-wrapper {{
            max-width: 1240px;
            margin: 0 auto;
        }}

        /* Top Bar */
        .top-navbar {{
            display: flex;
            align-items: center;
            justify-content: space-between;
            background: var(--card-bg);
            border: 1px solid var(--line);
            border-radius: 16px;
            padding: 14px 22px;
            margin-bottom: 20px;
            box-shadow: var(--shadow-sm);
            flex-wrap: wrap;
            gap: 12px;
        }}

        .nav-brand {{
            display: flex;
            align-items: center;
            gap: 14px;
        }}

        .nav-logo {{
            width: 48px;
            height: 48px;
            border-radius: 14px;
            background: var(--gold-gradient);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            box-shadow: 0 3px 10px rgba(92, 59, 36, 0.2);
            color: #FFFFFF;
        }}

        .nav-title {{
            font-family: Georgia, serif;
            font-size: 20px;
            font-weight: 800;
            color: var(--brown-primary);
            line-height: 1.2;
        }}

        .nav-subtitle {{
            font-size: 12px;
            color: var(--muted);
            font-weight: 600;
        }}

        .nav-badges {{
            display: flex;
            align-items: center;
            gap: 8px;
            flex-wrap: wrap;
        }}

        .badge-pill {{
            display: inline-flex;
            align-items: center;
            gap: 5px;
            background: var(--brown-soft);
            color: var(--brown-primary);
            font-size: 11px;
            font-weight: 700;
            padding: 5px 12px;
            border-radius: 20px;
            border: 1px solid var(--line);
        }}

        .nav-actions {{
            display: flex;
            align-items: center;
            gap: 10px;
        }}

        .lang-switch {{
            display: flex;
            background: var(--brown-soft);
            padding: 3px;
            border-radius: 20px;
            border: 1px solid var(--line);
        }}

        .lang-btn {{
            background: transparent;
            border: none;
            padding: 5px 11px;
            font-size: 11.5px;
            font-weight: 700;
            color: var(--brown-primary);
            border-radius: 16px;
            cursor: pointer;
            transition: all 0.2s ease;
        }}

        .lang-btn.active {{
            background: var(--brown-primary);
            color: #FFFFFF;
            box-shadow: 0 2px 6px rgba(92, 59, 36, 0.25);
        }}

        .btn-header {{
            display: inline-flex;
            align-items: center;
            gap: 6px;
            background: var(--card-bg);
            border: 1.5px solid var(--brown-primary);
            color: var(--brown-primary);
            padding: 7px 15px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 700;
            cursor: pointer;
            text-decoration: none;
            transition: all 0.2s ease;
        }}

        .btn-header:hover {{
            background: var(--brown-soft);
        }}

        .btn-header.primary {{
            background: var(--brown-primary);
            color: #FFFFFF;
        }}

        .btn-header.primary:hover {{
            background: var(--brown-dark);
        }}

        /* Metrics Banner */
        .metrics-banner {{
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 14px;
            margin-bottom: 24px;
        }}

        .metric-tile {{
            background: var(--card-bg);
            border: 1px solid var(--line);
            border-radius: 16px;
            padding: 18px 14px;
            text-align: center;
            box-shadow: var(--shadow-sm);
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }}

        .metric-tile:hover {{
            transform: translateY(-2px);
            box-shadow: var(--shadow-md);
        }}

        .metric-val {{
            font-family: Georgia, serif;
            font-size: 34px;
            font-weight: 800;
            color: var(--brown-primary);
            line-height: 1;
        }}

        .metric-title {{
            font-size: 12.5px;
            font-weight: 800;
            color: var(--ink);
            margin-top: 6px;
        }}

        .metric-sub {{
            font-size: 10.5px;
            color: var(--muted);
            margin-top: 2px;
        }}

        /* 2-Column Desktop Grid */
        .desktop-grid {{
            display: grid;
            grid-template-columns: 1.6fr 1fr;
            gap: 24px;
            align-items: start;
        }}

        /* Content Cards */
        .panel-card {{
            background: var(--card-bg);
            border: 1px solid var(--line);
            border-radius: 18px;
            padding: 22px 20px;
            box-shadow: var(--shadow-sm);
            margin-bottom: 24px;
        }}

        .panel-header {{
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 16px;
            padding-bottom: 12px;
            border-bottom: 1px solid var(--line-subtle);
            flex-wrap: wrap;
            gap: 8px;
        }}

        .panel-title {{
            font-family: Georgia, serif;
            font-size: 17px;
            font-weight: 800;
            color: var(--brown-primary);
            display: flex;
            align-items: center;
            gap: 8px;
        }}

        .status-badge {{
            background: var(--green-bg);
            color: var(--green);
            border: 1px solid var(--green-line);
            padding: 3px 10px;
            border-radius: 12px;
            font-size: 11px;
            font-weight: 800;
        }}

        /* Timeline Navigation Tabs */
        .timeline-tabs {{
            display: flex;
            gap: 8px;
            background: var(--brown-soft);
            padding: 4px;
            border-radius: 14px;
            margin-bottom: 18px;
            border: 1px solid var(--line);
        }}

        .t-tab {{
            flex: 1;
            padding: 10px 14px;
            border: none;
            background: transparent;
            font-size: 13px;
            font-weight: 700;
            color: var(--brown-primary);
            border-radius: 10px;
            cursor: pointer;
            transition: all 0.2s ease;
            text-align: center;
        }}

        .t-tab.active {{
            background: #FFFFFF;
            color: var(--brown-dark);
            box-shadow: 0 2px 8px rgba(92, 59, 36, 0.12);
            font-weight: 800;
        }}

        /* Step Card */
        .steps-container {{
            display: flex;
            flex-direction: column;
            gap: 12px;
        }}

        .step-item {{
            display: flex;
            gap: 14px;
            background: #FFFFFF;
            border: 1px solid var(--line-subtle);
            border-radius: 14px;
            padding: 14px 16px;
            transition: all 0.2s ease;
            position: relative;
        }}

        .step-item:hover {{
            border-color: var(--line);
            box-shadow: 0 4px 14px rgba(92, 59, 36, 0.06);
            transform: translateY(-1px);
        }}

        .step-item.completed {{
            border-left: 4px solid var(--green);
        }}

        .step-item.highlight {{
            border-left: 4px solid var(--brown-primary);
            background: #FFFDFB;
        }}

        .step-icon-col {{
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 6px;
            flex-shrink: 0;
        }}

        .step-icon {{
            width: 40px;
            height: 40px;
            border-radius: 12px;
            background: var(--brown-soft);
            border: 1px solid var(--line);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
        }}

        .step-num-badge {{
            font-size: 9.5px;
            font-weight: 800;
            color: var(--muted);
            background: var(--cream-bg);
            padding: 2px 6px;
            border-radius: 6px;
            border: 1px solid var(--line-subtle);
        }}

        .step-main {{
            flex: 1;
            min-width: 0;
        }}

        .step-head-row {{
            display: flex;
            align-items: flex-start;
            justify-content: space-between;
            gap: 10px;
            margin-bottom: 4px;
        }}

        .step-heading {{
            font-size: 14px;
            font-weight: 800;
            color: var(--brown-dark);
            line-height: 1.3;
        }}

        .step-meta-chips {{
            display: flex;
            align-items: center;
            flex-wrap: wrap;
            gap: 8px;
            margin-bottom: 6px;
        }}

        .chip-date {{
            font-size: 11px;
            font-weight: 800;
            color: var(--brown-primary);
        }}

        .chip-time {{
            background: var(--brown-soft);
            color: var(--brown-dark);
            font-size: 10.5px;
            font-weight: 800;
            padding: 2px 8px;
            border-radius: 8px;
        }}

        .chip-loc {{
            font-size: 10.5px;
            color: var(--muted);
            font-weight: 600;
        }}

        .step-text {{
            font-size: 12px;
            color: #4A3E37;
            line-height: 1.5;
        }}

        .dosage-tag {{
            display: inline-block;
            background: #FDF5EA;
            border: 1px solid #E6CA8D;
            color: var(--brown-primary);
            font-size: 11px;
            font-weight: 700;
            padding: 2px 8px;
            border-radius: 6px;
            margin-top: 6px;
        }}

        .step-pill-right {{
            flex-shrink: 0;
            text-align: right;
        }}

        .pill-done {{
            background: var(--green-bg);
            color: var(--green);
            border: 1px solid var(--green-line);
            padding: 3px 8px;
            border-radius: 8px;
            font-size: 10px;
            font-weight: 800;
            white-space: nowrap;
        }}

        .pill-countdown {{
            background: #FFF7EC;
            border: 1px solid #E6CA8D;
            padding: 4px 8px;
            border-radius: 10px;
            text-align: center;
            min-width: 56px;
        }}

        .countdown-val {{
            font-family: Georgia, serif;
            font-size: 18px;
            font-weight: 800;
            color: var(--brown-primary);
            line-height: 1;
        }}

        .countdown-lbl {{
            font-size: 8px;
            font-weight: 800;
            color: var(--muted);
            letter-spacing: 0.5px;
        }}

        /* Photo Gallery */
        .gallery-grid {{
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 12px;
            margin-top: 14px;
        }}

        .gallery-card {{
            background: var(--cream-bg);
            border: 1px solid var(--line);
            border-radius: 14px;
            overflow: hidden;
            cursor: pointer;
            transition: all 0.2s ease;
            display: flex;
            flex-direction: column;
        }}

        .gallery-card:hover {{
            transform: translateY(-2px);
            box-shadow: var(--shadow-md);
            border-color: var(--brown-primary);
        }}

        .gallery-img-wrap {{
            height: 115px;
            background: #FFFFFF;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
            border-bottom: 1px solid var(--line-subtle);
        }}

        .gallery-img-wrap img {{
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.3s ease;
        }}

        .gallery-card:hover .gallery-img-wrap img {{
            transform: scale(1.05);
        }}

        .gallery-body {{
            padding: 8px 10px;
        }}

        .gallery-date {{
            font-size: 9.5px;
            font-weight: 800;
            color: var(--brown-primary);
        }}

        .gallery-name {{
            font-size: 11.5px;
            font-weight: 800;
            color: var(--ink);
            margin: 2px 0;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }}

        /* Right Column Widgets */
        /* Personal Notes */
        .mood-chips {{
            display: flex;
            gap: 6px;
            flex-wrap: wrap;
            margin-bottom: 10px;
        }}

        .mood-pill {{
            background: #FFFFFF;
            border: 1px solid var(--line);
            border-radius: 16px;
            padding: 4px 10px;
            font-size: 11px;
            font-weight: 700;
            color: var(--brown-primary);
            cursor: pointer;
            transition: all 0.15s ease;
        }}

        .mood-pill.active {{
            background: var(--brown-soft);
            border-color: var(--brown-primary);
            font-weight: 800;
        }}

        .notes-textarea {{
            width: 100%;
            min-height: 115px;
            border: 1.5px solid var(--line);
            border-radius: 12px;
            padding: 12px;
            font-family: inherit;
            font-size: 12.5px;
            color: var(--ink);
            background: #FFFFFF;
            resize: vertical;
            line-height: 1.5;
            transition: border-color 0.2s ease;
        }}

        .notes-textarea:focus {{
            outline: none;
            border-color: var(--brown-primary);
        }}

        .notes-toolbar {{
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-top: 8px;
            flex-wrap: wrap;
            gap: 8px;
        }}

        .save-status {{
            font-size: 11px;
            font-weight: 700;
            color: var(--green);
        }}

        .notes-btns {{
            display: flex;
            gap: 6px;
        }}

        .btn-note {{
            background: #FFFFFF;
            border: 1px solid var(--line);
            border-radius: 8px;
            padding: 6px 10px;
            font-size: 11.5px;
            font-weight: 700;
            color: var(--brown-primary);
            cursor: pointer;
            transition: all 0.15s ease;
        }}

        .btn-note:hover {{
            background: var(--brown-soft);
        }}

        .btn-note.primary {{
            background: var(--brown-primary);
            color: #FFFFFF;
            border-color: var(--brown-primary);
        }}

        .btn-note.primary:hover {{
            background: var(--brown-dark);
        }}

        .saved-notes-feed {{
            margin-top: 14px;
            max-height: 250px;
            overflow-y: auto;
            display: flex;
            flex-direction: column;
            gap: 8px;
            padding-right: 4px;
        }}

        .saved-note-bubble {{
            background: #FFFFFF;
            border: 1px solid var(--line-subtle);
            border-radius: 12px;
            padding: 10px 12px;
            position: relative;
        }}

        .bubble-head {{
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 10px;
            font-weight: 700;
            color: var(--muted);
            margin-bottom: 4px;
        }}

        .bubble-del {{
            background: transparent;
            border: none;
            color: #A99B93;
            cursor: pointer;
            font-size: 13px;
            padding: 0 4px;
            line-height: 1;
        }}

        .bubble-del:hover {{
            color: #B52828;
        }}

        .bubble-text {{
            font-size: 11.5px;
            color: var(--ink);
            line-height: 1.45;
            white-space: pre-wrap;
        }}

        /* Checklist */
        .checklist-group {{
            display: flex;
            flex-direction: column;
            gap: 8px;
        }}

        .check-item {{
            display: flex;
            align-items: center;
            gap: 12px;
            background: #FFFFFF;
            border: 1px solid var(--line-subtle);
            border-radius: 12px;
            padding: 10px 12px;
            cursor: pointer;
            user-select: none;
            transition: all 0.15s ease;
        }}

        .check-item:hover {{
            border-color: var(--line);
            background: #FFFDFB;
        }}

        .check-box {{
            width: 22px;
            height: 22px;
            border-radius: 7px;
            border: 2px solid var(--brown-primary);
            display: flex;
            align-items: center;
            justify-content: center;
            color: transparent;
            font-size: 13px;
            font-weight: 900;
            flex-shrink: 0;
            transition: all 0.15s ease;
        }}

        .check-item.checked .check-box {{
            background: var(--green);
            border-color: var(--green);
            color: #FFFFFF;
        }}

        .check-item.checked .check-title {{
            text-decoration: line-through;
            color: var(--muted);
        }}

        .check-title {{
            font-size: 12.5px;
            font-weight: 800;
            color: var(--ink);
        }}

        .check-sub {{
            font-size: 10.5px;
            color: var(--muted);
            margin-top: 1px;
        }}

        /* Clinic Info Card */
        .clinic-info-list {{
            display: flex;
            flex-direction: column;
            gap: 12px;
        }}

        .clinic-item {{
            display: flex;
            gap: 10px;
            font-size: 11.5px;
            line-height: 1.45;
        }}

        .clinic-icon {{
            font-size: 16px;
            flex-shrink: 0;
        }}

        .clinic-name {{
            font-weight: 800;
            color: var(--brown-primary);
        }}

        /* Lightbox Modal */
        .modal-overlay {{
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(43, 35, 32, 0.75);
            display: none;
            align-items: center;
            justify-content: center;
            z-index: 9999;
            padding: 20px;
            backdrop-filter: blur(4px);
        }}

        .modal-overlay.open {{
            display: flex;
        }}

        .modal-box {{
            background: #FFFFFF;
            border-radius: 18px;
            max-width: 580px;
            width: 100%;
            overflow: hidden;
            box-shadow: var(--shadow-lg);
            position: relative;
            animation: popIn 0.2s ease-out;
        }}

        @keyframes popIn {{
            from {{ transform: scale(0.95); opacity: 0; }}
            to {{ transform: scale(1); opacity: 1; }}
        }}

        .modal-img-wrap {{
            height: 240px;
            background: var(--cream-bg);
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
        }}

        .modal-img-wrap img {{
            width: 100%;
            height: 100%;
            object-fit: cover;
        }}

        .modal-close-btn {{
            position: absolute;
            top: 12px;
            right: 12px;
            width: 32px;
            height: 32px;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.9);
            border: 1px solid var(--line);
            color: var(--ink);
            font-size: 16px;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            box-shadow: 0 2px 8px rgba(0,0,0,0.15);
        }}

        .modal-content {{
            padding: 18px 20px;
        }}

        .modal-stage {{
            font-size: 11px;
            font-weight: 800;
            color: var(--brown-primary);
            margin-bottom: 2px;
        }}

        .modal-title {{
            font-family: Georgia, serif;
            font-size: 18px;
            font-weight: 800;
            color: var(--brown-dark);
            margin-bottom: 8px;
        }}

        .modal-desc {{
            font-size: 12.5px;
            color: #4A3E37;
            line-height: 1.55;
        }}

        /* Footer */
        .desktop-footer {{
            margin-top: 24px;
            text-align: center;
            font-size: 11px;
            color: var(--muted);
            padding: 16px 0;
            border-top: 1px solid var(--line);
        }}

        /* Responsive Breakpoints */
        @media (max-width: 980px) {{
            .desktop-grid {{
                grid-template-columns: 1fr;
            }}
            .metrics-banner {{
                grid-template-columns: repeat(2, 1fr);
            }}
            .gallery-grid {{
                grid-template-columns: repeat(2, 1fr);
            }}
        }}

        @media (max-width: 600px) {{
            body {{ padding: 12px 8px; }}
            .metrics-banner {{ grid-template-columns: 1fr; }}
            .gallery-grid {{ grid-template-columns: 1fr; }}
            .top-navbar {{ flex-direction: column; align-items: flex-start; }}
            .nav-actions {{ width: 100%; justify-content: space-between; }}
        }}

        /* Print Media */
        @media print {{
            body {{ background: #FFFFFF; padding: 0; }}
            .top-navbar .nav-actions, .timeline-tabs, .mood-chips, .notes-toolbar, .btn-note, .modal-overlay {{ display: none !important; }}
            .desktop-wrapper {{ max-width: 100%; }}
            .desktop-grid {{ display: block; }}
            .panel-card {{ border: none; box-shadow: none; padding: 10px 0; }}
            #september-steps, #upcoming-steps {{ display: flex !important; }}
        }}
    </style>
</head>
<body>
    <div class="desktop-wrapper">
        <!-- Top Navbar -->
        <header class="top-navbar">
            <div class="nav-brand">
                <div class="nav-logo">🤰</div>
                <div>
                    <h1 class="nav-title" id="header-title">Mon Parcours FIV</h1>
                    <div class="nav-subtitle" id="header-subtitle">Fatou ♥ · RMA of New York (East Side) & Gramercy Surgery Center</div>
                </div>
            </div>

            <div class="nav-badges">
                <span class="badge-pill" id="badge-pin">🔒 Code PIN · 1998</span>
                <span class="badge-pill" id="badge-dr">🩺 Dr. Coperman</span>
                <span class="badge-pill" style="background:#FAF2EA; color:#7A4F31;">🖥️ Grand Écran · Brown</span>
            </div>

            <div class="nav-actions">
                <div class="lang-switch">
                    <button type="button" class="lang-btn active" id="btn-fr" onclick="switchLanguage('fr')">🇫🇷 FR</button>
                    <button type="button" class="lang-btn" id="btn-en" onclick="switchLanguage('en')">🇬🇧 EN</button>
                </div>
                <button type="button" class="btn-header" onclick="window.print()">
                    <span>📄</span>
                    <span id="btn-pdf-text">Imprimer / PDF</span>
                </button>
                <a class="btn-header primary" href="https://litter.catbox.moe/z1ni2q.apk" target="_blank" rel="noopener noreferrer">
                    <span>📥</span>
                    <span id="btn-apk-text">APK Android</span>
                </a>
            </div>
        </header>

        <!-- Key Metrics Banner -->
        <section class="metrics-banner">
            <div class="metric-tile">
                <div class="metric-val">42</div>
                <div class="metric-title" id="lbl-oocytes">Ovocytes Prélevés</div>
                <div class="metric-sub">Ponction OPU · 18/09/2026</div>
            </div>
            <div class="metric-tile">
                <div class="metric-val">34</div>
                <div class="metric-title" id="lbl-mature">Ovocytes Matures (MII)</div>
                <div class="metric-sub">Taux de maturité : 81 %</div>
            </div>
            <div class="metric-tile">
                <div class="metric-val">28</div>
                <div class="metric-title" id="lbl-fertilized">Fécondés par ICSI</div>
                <div class="metric-sub">Taux de fécondation : 82 %</div>
            </div>
            <div class="metric-tile">
                <div class="metric-val">14</div>
                <div class="metric-title" id="lbl-blastocysts">Blastocystes Vitrifiés</div>
                <div class="metric-sub">Biopsiés pour analyse PGT-A</div>
            </div>
        </section>

        <!-- Main 2-Column Desktop Grid -->
        <main class="desktop-grid">
            <!-- Left Column: Protocol Timeline & Photo Journal -->
            <div class="left-column">
                <!-- Protocol Section -->
                <section class="panel-card">
                    <div class="panel-header">
                        <div class="panel-title">
                            <span>🔬</span>
                            <span id="proto-panel-title">Protocole Médical FIV</span>
                        </div>
                        <span class="status-badge" id="proto-badge">15 ÉTAPES TERMINÉES</span>
                    </div>

                    <!-- Timeline Tabs -->
                    <div class="timeline-tabs">
                        <button type="button" class="t-tab active" id="tab-sept" onclick="switchTab('sept')">
                            Septembre 2026 (Stimulation - 15 étapes)
                        </button>
                        <button type="button" class="t-tab" id="tab-up" onclick="switchTab('up')">
                            Octobre & Novembre (Grossesse & FET - 7 étapes)
                        </button>
                    </div>

                    <!-- September Stimulation Steps (15 Steps) -->
                    <div class="steps-container" id="september-steps">
                        <!-- Step 1 -->
                        <div class="step-item completed">
                            <div class="step-icon-col"><div class="step-icon">🩸</div><span class="step-num-badge">01</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Prise de sang & Échographie basale</div><div class="step-pill-right"><span class="pill-done">✓ FAIT</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">04 sept. 2026</span><span class="chip-time">08 h 00</span><span class="chip-loc">📍 RMA East Side (Dr. Coperman)</span></div>
                                <p class="step-text">Bilan hormonal complet (Estradiol, LH, Progestérone, FSH) et échographie pelvienne de comptage des follicules antraux (CFA). Feu vert officiel pour le protocole de stimulation ovarienne.</p>
                            </div>
                        </div>

                        <!-- Step 2 -->
                        <div class="step-item completed highlight">
                            <div class="step-icon-col"><div class="step-icon">💉</div><span class="step-num-badge">02</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Démarrage Follistim 225 UI + Menopur 150 UI</div><div class="step-pill-right"><span class="pill-done">✓ FAIT</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">05 sept. 2026</span><span class="chip-time">20 h 00</span><span class="chip-loc">📍 Domicile</span></div>
                                <p class="step-text">Début des injections sous-cutanées quotidiennes à heure fixe chaque soir dans l'abdomen pour stimuler la croissance harmonieuse d'une cohorte folliculaire optimale.</p>
                                <span class="dosage-tag">💊 Follistim 225 UI + Menopur 150 UI (1er soir)</span>
                            </div>
                        </div>

                        <!-- Step 3 -->
                        <div class="step-item completed">
                            <div class="step-icon-col"><div class="step-icon">🩺</div><span class="step-num-badge">03</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Monitorage sanguin Estradiol & Échographie</div><div class="step-pill-right"><span class="pill-done">✓ FAIT</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">09 sept. 2026</span><span class="chip-time">07 h 15</span><span class="chip-loc">📍 RMA East Side</span></div>
                                <p class="step-text">Premier contrôle de la réponse ovarienne après 4 jours de stimulation. Évaluation du pic d'œstradiol et mesure du diamètre des follicules en croissance active bilatérale.</p>
                            </div>
                        </div>

                        <!-- Step 4 -->
                        <div class="step-item completed highlight">
                            <div class="step-icon-col"><div class="step-icon">🛡️</div><span class="step-num-badge">04</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Monitorage folliculaire & Ajout Ganirelix 250 mcg</div><div class="step-pill-right"><span class="pill-done">✓ FAIT</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">11 sept. 2026</span><span class="chip-time">07 h 30</span><span class="chip-loc">📍 RMA East Side</span></div>
                                <p class="step-text">Contrôle folliculaire échographique et introduction de l'antagoniste GnRH Ganirelix pour bloquer tout pic prématuré de LH et protéger l'ovulation spontanée.</p>
                                <span class="dosage-tag">💊 Ajout Ganirelix 250 mcg chaque matin</span>
                            </div>
                        </div>

                        <!-- Step 5 -->
                        <div class="step-item completed">
                            <div class="step-icon-col"><div class="step-icon">🔬</div><span class="step-num-badge">05</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Échographie folliculaire (> 14 mm)</div><div class="step-pill-right"><span class="pill-done">✓ FAIT</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">13 sept. 2026</span><span class="chip-time">08 h 00</span><span class="chip-loc">📍 RMA East Side</span></div>
                                <p class="step-text">Nombreux follicules dominants mesurés entre 14 et 17 mm. Ajustement minutieux des doses par le Dr Coperman pour assurer une maturation synchrone et douce.</p>
                            </div>
                        </div>

                        <!-- Step 6 -->
                        <div class="step-item completed">
                            <div class="step-icon-col"><div class="step-icon">🧪</div><span class="step-num-badge">06</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Contrôle final de maturation folliculaire</div><div class="step-pill-right"><span class="pill-done">✓ FAIT</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">15 sept. 2026</span><span class="chip-time">07 h 30</span><span class="chip-loc">📍 RMA East Side</span></div>
                                <p class="step-text">Dernière échographie de contrôle avant déclenchement. Mesure de follicules matures prêts (18-22 mm) et contrôle de sécurité pour prévenir le syndrome d'hyperstimulation (OHSS).</p>
                            </div>
                        </div>

                        <!-- Step 7 -->
                        <div class="step-item completed highlight">
                            <div class="step-icon-col"><div class="step-icon">⭐</div><span class="step-num-badge">07</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Déclenchement ovocytaire (Double Trigger)</div><div class="step-pill-right"><span class="pill-done">✓ FAIT</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">16 sept. 2026</span><span class="chip-time">23 h 00 pile</span><span class="chip-loc">📍 Domicile</span></div>
                                <p class="step-text">Injection exacte de Lupron 80 UI et Ovidrel 250 mcg 36 heures précises avant la ponction chirurgicale. Déclenchement de la méiose ovocytaire finale.</p>
                                <span class="dosage-tag">⏰ 23h00 précise · Double Trigger Lupron + Ovidrel</span>
                            </div>
                        </div>

                        <!-- Step 8 -->
                        <div class="step-item completed highlight">
                            <div class="step-icon-col"><div class="step-icon">🏥</div><span class="step-num-badge">08</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Ponction Ovocytaire OPU (Gramercy Center)</div><div class="step-pill-right"><span class="pill-done" style="background:#2B7853; color:#FFFFFF;">42 OVOCYTES !</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">18 sept. 2026</span><span class="chip-time">07 h 00</span><span class="chip-loc">📍 Gramercy Surgery Center (380 2nd Ave)</span></div>
                                <p class="step-text">Intervention sous anesthésie douce réalisée par le Dr Coperman. Prélèvement exceptionnel de 42 ovocytes. Récupération post-anesthésie exemplaire et retour au domicile le matin même.</p>
                            </div>
                        </div>

                        <!-- Step 9 -->
                        <div class="step-item completed">
                            <div class="step-icon-col"><div class="step-icon">🧫</div><span class="step-num-badge">09</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Bilan Fécondation J+1 ICSI (Laboratoire)</div><div class="step-pill-right"><span class="pill-done">28 FÉCONDÉS</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">19 sept. 2026</span><span class="chip-time">10 h 00</span><span class="chip-loc">📍 RMA Embryology Lab</span></div>
                                <p class="step-text">Appel du laboratoire d'embryologie : 34 ovocytes matures identifiés (MII, 81 %) et 28 embryons fécondés avec succès par micro-injection intra-cytoplasmique de spermatozoïdes (ICSI).</p>
                            </div>
                        </div>

                        <!-- Step 10 -->
                        <div class="step-item completed">
                            <div class="step-icon-col"><div class="step-icon">🌱</div><span class="step-num-badge">10</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Contrôle embryonnaire J+3 (Clivage)</div><div class="step-pill-right"><span class="pill-done">24 EMBRYONS</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">21 sept. 2026</span><span class="chip-time">11 h 00</span><span class="chip-loc">📍 RMA Embryology Lab</span></div>
                                <p class="step-text">24 embryons en développement cellulaire actif régulier (6 à 8 cellules avec faible fragmentation). Poursuite de la culture prolongée vers le stade blastocyste.</p>
                            </div>
                        </div>

                        <!-- Step 11 -->
                        <div class="step-item completed highlight">
                            <div class="step-icon-col"><div class="step-icon">❄️</div><span class="step-num-badge">11</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Biopsie J+5 & Congélation Blastocystes</div><div class="step-pill-right"><span class="pill-done">1ER LOT</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">23 sept. 2026</span><span class="chip-time">14 h 00</span><span class="chip-loc">📍 RMA Embryology Lab</span></div>
                                <p class="step-text">Biopsie délicate de cellules du trophoectoderme (futur placenta) sans toucher à la masse cellulaire interne. Vitrification ultra-rapide des premiers blastocystes expansés.</p>
                            </div>
                        </div>

                        <!-- Step 12 -->
                        <div class="step-item completed highlight">
                            <div class="step-icon-col"><div class="step-icon">🏆</div><span class="step-num-badge">12</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Rapport final J+6 (14 Blastocystes Vitrifiés)</div><div class="step-pill-right"><span class="pill-done" style="background:#2B7853; color:#FFFFFF;">14 BLASTOS !</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">24 sept. 2026</span><span class="chip-time">10 h 00</span><span class="chip-loc">📍 RMA Embryology Lab</span></div>
                                <p class="step-text">Résultat final de la culture prolongée : 14 blastocystes de haute qualité vitrifiés et stockés en toute sécurité dans l'azote liquide à -196°C pour vos futurs transferts.</p>
                            </div>
                        </div>

                        <!-- Step 13 -->
                        <div class="step-item completed">
                            <div class="step-icon-col"><div class="step-icon">📬</div><span class="step-num-badge">13</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Expédition échantillons pour analyse PGT-A</div><div class="step-pill-right"><span class="pill-done">✓ ENVOYÉ</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">25 sept. 2026</span><span class="chip-time">16 h 00</span><span class="chip-loc">📍 Laboratoire de Génétique</span></div>
                                <p class="step-text">Envoi sécurisé sous chaîne du froid des biopsies embryonnaires pour le criblage chromosomique complet (détection des embryons euploïdes équilibrés).</p>
                            </div>
                        </div>

                        <!-- Step 14 -->
                        <div class="step-item completed">
                            <div class="step-icon-col"><div class="step-icon">🌸</div><span class="step-num-badge">14</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Consultation de récupération post-ponction</div><div class="step-pill-right"><span class="pill-done">✓ RÉCUPÉRÉ</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">28 sept. 2026</span><span class="chip-time">11 h 30</span><span class="chip-loc">📍 RMA East Side</span></div>
                                <p class="step-text">Examen clinique de contrôle avec le Dr Coperman. Résorption normale de l'œdème ovarien, excellente récupération physique et hydratation optimale confirmée.</p>
                            </div>
                        </div>

                        <!-- Step 15 -->
                        <div class="step-item completed">
                            <div class="step-icon-col"><div class="step-icon">🏁</div><span class="step-num-badge">15</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Clôture officielle de la phase de stimulation</div><div class="step-pill-right"><span class="pill-done">✓ VALIDÉ</span></div></div>
                                <div class="step-meta-row"><span class="chip-date">30 sept. 2026</span><span class="chip-time">Bilan</span><span class="chip-loc">📍 RMA of New York</span></div>
                                <p class="step-text">Validation du succès éclatant du protocole de stimulation : 42 ovocytes prélevés, 14 blastocystes sécurisés. Transition vers la phase de préparation endométriale et transfert.</p>
                            </div>
                        </div>
                    </div>

                    <!-- Upcoming Pregnancy Milestones (7 Steps) -->
                    <div class="steps-container" id="upcoming-steps" style="display: none;">
                        <!-- Up 1 -->
                        <div class="step-item highlight">
                            <div class="step-icon-col"><div class="step-icon">🧬</div><span class="step-num-badge">01</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Résultats PGT-A (8 blastocystes)</div><div class="step-pill-right"><div class="pill-countdown"><div class="countdown-val">2</div><div class="countdown-lbl">JOURS</div></div></div></div>
                                <div class="step-meta-row"><span class="chip-date">27 oct. 2026</span><span class="chip-time">14 h 00</span><span class="chip-loc">📍 RMA of New York</span></div>
                                <p class="step-text">Réception du compte-rendu génétique complet précisant le nombre d'embryons euploïdes chromosomiquement sains prêts pour le transfert.</p>
                            </div>
                        </div>

                        <!-- Up 2 -->
                        <div class="step-item highlight">
                            <div class="step-icon-col"><div class="step-icon">🔬</div><span class="step-num-badge">02</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Hystérosonographie SIS (Examen cavité)</div><div class="step-pill-right"><div class="pill-countdown"><div class="countdown-val">3</div><div class="countdown-lbl">JOURS</div></div></div></div>
                                <div class="step-meta-row"><span class="chip-date">28 oct. 2026</span><span class="chip-time">08 h 00</span><span class="chip-loc">📍 RMA East Side (635 Madison Ave)</span></div>
                                <p class="step-text">Échographie de haute précision avec instillation de sérum physiologique pour cartographier parfaitement la cavité utérine et mesurer la réceptivité endométriale.</p>
                            </div>
                        </div>

                        <!-- Up 3 -->
                        <div class="step-item">
                            <div class="step-icon-col"><div class="step-icon">🏥</div><span class="step-num-badge">03</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Hystéroscopie opératoire diagnostique</div><div class="step-pill-right"><div class="pill-countdown"><div class="countdown-val">5</div><div class="countdown-lbl">JOURS</div></div></div></div>
                                <div class="step-meta-row"><span class="chip-date">30 oct. 2026</span><span class="chip-time">09 h 00</span><span class="chip-loc">📍 Gramercy Surgery Center</span></div>
                                <p class="step-text">Préparation de surface millimétrique de l'endomètre et vérification approfondie sous caméra pour garantir des conditions d'implantation optimales.</p>
                            </div>
                        </div>

                        <!-- Up 4 -->
                        <div class="step-item highlight">
                            <div class="step-icon-col"><div class="step-icon">🌟</div><span class="step-num-badge">04</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Transfert d'Embryon Congelé (FET)</div><div class="step-pill-right"><div class="pill-countdown"><div class="countdown-val">6</div><div class="countdown-lbl">JOURS</div></div></div></div>
                                <div class="step-meta-row"><span class="chip-date">31 oct. 2026</span><span class="chip-time">Midi</span><span class="chip-loc">📍 RMA East Side</span></div>
                                <p class="step-text">Le grand moment tant attendu : décongélation et transfert délicat du meilleur blastocyste euploïde sous guidage échographique dans une cavité idéale.</p>
                            </div>
                        </div>

                        <!-- Up 5 -->
                        <div class="step-item highlight">
                            <div class="step-icon-col"><div class="step-icon">🩸</div><span class="step-num-badge">05</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">Prise de sang Bêta-hCG (Test de Grossesse)</div><div class="step-pill-right"><div class="pill-countdown"><div class="countdown-val">14</div><div class="countdown-lbl">JOURS</div></div></div></div>
                                <div class="step-meta-row"><span class="chip-date">08 nov. 2026</span><span class="chip-time">07 h 30</span><span class="chip-loc">📍 RMA East Side</span></div>
                                <p class="step-text">Dosage plasmatique officiel de l'hormone de grossesse hCG confirmant l'implantation embryonnaire et le début officiel de la grossesse.</p>
                            </div>
                        </div>

                        <!-- Up 6 -->
                        <div class="step-item">
                            <div class="step-icon-col"><div class="step-icon">📈</div><span class="step-num-badge">06</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">2ème Dosage Bêta-hCG (Cinétique)</div><div class="step-pill-right"><div class="pill-countdown"><div class="countdown-val">16</div><div class="countdown-lbl">JOURS</div></div></div></div>
                                <div class="step-meta-row"><span class="chip-date">10 nov. 2026</span><span class="chip-time">08 h 00</span><span class="chip-loc">📍 RMA East Side</span></div>
                                <p class="step-text">Contrôle de la cinétique hormonale : vérification du bon doublement du taux d'hCG en 48 heures, gage d'une évolution embryonnaire vigoureuse.</p>
                            </div>
                        </div>

                        <!-- Up 7 -->
                        <div class="step-item highlight">
                            <div class="step-icon-col"><div class="step-icon">💓</div><span class="step-num-badge">07</span></div>
                            <div class="step-main">
                                <div class="step-head-row"><div class="step-heading">1ère Échographie (Battement de cœur)</div><div class="step-pill-right"><div class="pill-countdown"><div class="countdown-val">28</div><div class="countdown-lbl">JOURS</div></div></div></div>
                                <div class="step-meta-row"><span class="chip-date">22 nov. 2026</span><span class="chip-time">09 h 30</span><span class="chip-loc">📍 RMA East Side (6-7 SA)</span></div>
                                <p class="step-text">Échographie de viabilité fœtale : premier visuel du sac gestationnel et écoute émouvante du premier battement de cœur de votre futur bébé.</p>
                            </div>
                        </div>
                    </div>
                </section>

                <!-- Photo Journal Gallery -->
                <section class="panel-card">
                    <div class="panel-header">
                        <div class="panel-title">
                            <span>📸</span>
                            <span id="gallery-title">Journal Clinique Illustré</span>
                        </div>
                        <span class="badge-pill">5 CLICHÉS CLINIQUES</span>
                    </div>
                    <p class="step-text" id="gallery-sub">Sélectionnez une vignette pour visualiser les détails échographiques et embryonnaires.</p>

                    <div class="gallery-grid">
                        <!-- Item 1 -->
                        <div class="gallery-card" onclick="openLightbox('pgta')">
                            <div class="gallery-img-wrap">
                                <img src="{svgs['journal_pgta.svg']}" alt="PGT-A Blastocyste">
                            </div>
                            <div class="gallery-body">
                                <div class="gallery-date">27 Oct. 2026</div>
                                <div class="gallery-name">PGT-A Blastocystes</div>
                            </div>
                        </div>

                        <!-- Item 2 -->
                        <div class="gallery-card" onclick="openLightbox('sis')">
                            <div class="gallery-img-wrap">
                                <img src="{svgs['journal_sis.svg']}" alt="SIS Échographie 3D">
                            </div>
                            <div class="gallery-body">
                                <div class="gallery-date">28 Oct. 2026</div>
                                <div class="gallery-name">SIS Échographie 3D</div>
                            </div>
                        </div>

                        <!-- Item 3 -->
                        <div class="gallery-card" onclick="openLightbox('transfer')">
                            <div class="gallery-img-wrap">
                                <img src="{svgs['journal_transfer.svg']}" alt="Transfert FET">
                            </div>
                            <div class="gallery-body">
                                <div class="gallery-date">31 Oct. 2026</div>
                                <div class="gallery-name">Transfert FET Implantation</div>
                            </div>
                        </div>

                        <!-- Item 4 -->
                        <div class="gallery-card" onclick="openLightbox('betahcg')">
                            <div class="gallery-img-wrap">
                                <img src="{svgs['journal_betahcg.svg']}" alt="Test Bêta-hCG">
                            </div>
                            <div class="gallery-body">
                                <div class="gallery-date">08 Nov. 2026</div>
                                <div class="gallery-name">Bêta-hCG Positif</div>
                            </div>
                        </div>

                        <!-- Item 5 -->
                        <div class="gallery-card" onclick="openLightbox('ultrasound')">
                            <div class="gallery-img-wrap">
                                <img src="{svgs['journal_ultrasound.svg']}" alt="Battement de cœur">
                            </div>
                            <div class="gallery-body">
                                <div class="gallery-date">22 Nov. 2026</div>
                                <div class="gallery-name">1er Battement de Cœur</div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>

            <!-- Right Column: Notes, Priorities Checklist, Clinic Contacts -->
            <div class="right-column">
                <!-- Notes Personnelles -->
                <section class="panel-card">
                    <div class="panel-header">
                        <div class="panel-title">
                            <span>📝</span>
                            <span id="notes-card-title">Notes personnelles de Fatou</span>
                        </div>
                        <span class="badge-pill" style="background:#FAF2EA; color:#7A4F31;">ESPACE PRIVÉ</span>
                    </div>

                    <p class="step-text" style="margin-bottom: 10px;" id="notes-card-desc">
                        Notez vos ressentis quotidiens, questions pour le médecin ou pensées du jour. Vos notes sont sauvegardées automatiquement.
                    </p>

                    <!-- Mood Selector -->
                    <div class="mood-chips" id="mood-box">
                        <button type="button" class="mood-pill active" onclick="setMood(this, '✨ Pleine d\\\'espoir')">✨ Pleine d'espoir</button>
                        <button type="button" class="mood-pill" onclick="setMood(this, '🌸 Sereine')">🌸 Sereine</button>
                        <button type="button" class="mood-pill" onclick="setMood(this, '💪 Confiante')">💪 Confiante</button>
                        <button type="button" class="mood-pill" onclick="setMood(this, '🙏 Reconnaissante')">🙏 Reconnaissante</button>
                        <button type="button" class="mood-pill" onclick="setMood(this, '🧘‍♀️ En repos')">🧘‍♀️ En repos</button>
                    </div>

                    <textarea id="notes-area" class="notes-textarea" placeholder="Chère Fatou, écrivez vos pensées du jour ici..."></textarea>

                    <div class="notes-toolbar">
                        <span class="save-status" id="notes-status">✓ Sauvegardé automatiquement</span>
                        <div class="notes-btns">
                            <button type="button" class="btn-note" onclick="insertDate()">📅 Date</button>
                            <button type="button" class="btn-note" onclick="clearDraft()">Effacer</button>
                            <button type="button" class="btn-note primary" onclick="saveToJournal()">Ajouter au journal</button>
                        </div>
                    </div>

                    <!-- Saved Notes History -->
                    <div class="saved-notes-feed" id="saved-notes-list"></div>
                </section>

                <!-- Priorités & Préparatifs Checklist -->
                <section class="panel-card">
                    <div class="panel-header">
                        <div class="panel-title">
                            <span>☑</span>
                            <span id="chk-card-title">Priorités & Préparatifs</span>
                        </div>
                        <span class="status-badge">6 ACTIONS</span>
                    </div>

                    <div class="checklist-group">
                        <div class="check-item" onclick="toggleCheck(this, 'chk_1')">
                            <div class="check-box">✓</div>
                            <div>
                                <div class="check-title">Dossier PGT-A & Consentements signés</div>
                                <div class="check-sub">27 oct. · Validation génétique des 8 blastocystes</div>
                            </div>
                        </div>

                        <div class="check-item" onclick="toggleCheck(this, 'chk_2')">
                            <div class="check-box">✓</div>
                            <div>
                                <div class="check-title">Rendez-vous SIS confirmé (RMA East Side)</div>
                                <div class="check-sub">28 oct. · 08 h 00 · Cavité utérine & endomètre</div>
                            </div>
                        </div>

                        <div class="check-item" onclick="toggleCheck(this, 'chk_3')">
                            <div class="check-box">✓</div>
                            <div>
                                <div class="check-title">Consultation pré-op Hystéroscopie</div>
                                <div class="check-sub">30 oct. · Gramercy Surgery Center</div>
                            </div>
                        </div>

                        <div class="check-item checked" onclick="toggleCheck(this, 'chk_4')">
                            <div class="check-box">✓</div>
                            <div>
                                <div class="check-title">Protocoles médicamenteux & Progestérone</div>
                                <div class="check-sub">En cours · Support lutéal et œstrogènes</div>
                            </div>
                        </div>

                        <div class="check-item" onclick="toggleCheck(this, 'chk_5')">
                            <div class="check-box">✓</div>
                            <div>
                                <div class="check-title">Préparer le jour du transfert FET</div>
                                <div class="check-sub">31 oct. · Vessie pleine, tenue confortable</div>
                            </div>
                        </div>

                        <div class="check-item" onclick="toggleCheck(this, 'chk_6')">
                            <div class="check-box">✓</div>
                            <div>
                                <div class="check-title">Test Bêta-hCG & suivi RMA</div>
                                <div class="check-sub">08 nov. · Prise de sang officielle de confirmation</div>
                            </div>
                        </div>
                    </div>
                </section>

                <!-- Medical Center & Contacts -->
                <section class="panel-card">
                    <div class="panel-header">
                        <div class="panel-title">
                            <span>🏥</span>
                            <span>Centres Médicaux & Contacts</span>
                        </div>
                        <span class="badge-pill">COORDONNÉES</span>
                    </div>

                    <div class="clinic-info-list">
                        <div class="clinic-item">
                            <div class="clinic-icon">📍</div>
                            <div>
                                <div class="clinic-name">RMA of New York (East Side)</div>
                                <div class="step-text">635 Madison Ave, 10th Floor, New York, NY 10022<br>Tél : (212) 756-5777 · Fax : (212) 756-5770</div>
                            </div>
                        </div>
                        <div class="clinic-item">
                            <div class="clinic-icon">🏥</div>
                            <div>
                                <div class="clinic-name">Gramercy Surgery Center</div>
                                <div class="step-text">380 2nd Avenue, 10th Floor, New York, NY 10010<br>Tél : (212) 254-2585 · Intervention OPU & Hystéroscopie</div>
                            </div>
                        </div>
                        <div class="clinic-item">
                            <div class="clinic-icon">🩺</div>
                            <div>
                                <div class="clinic-name">Équipe Clinique Référente</div>
                                <div class="step-text">Dr. Coperman · Infirmière coordinatrice FIV · Ligne d'urgence 24h/24 RMA</div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </main>

        <!-- Footer -->
        <footer class="desktop-footer" id="footer-note">
            Mon Parcours FIV · Fatou ♥ · RMA of New York (East Side) & Gramercy Surgery Center · Document confidentiel de suivi médical personnel · Édition Bureau / Desktop Brown
        </footer>
    </div>

    <!-- Lightbox Modal -->
    <div class="modal-overlay" id="lightbox-modal" onclick="closeLightbox(event)">
        <div class="modal-box" onclick="event.stopPropagation()">
            <div class="modal-img-wrap">
                <img id="modal-img" src="" alt="Agrandissement">
                <button type="button" class="modal-close-btn" onclick="closeLightbox(event)">✕</button>
            </div>
            <div class="modal-content">
                <div class="modal-stage" id="modal-stage"></div>
                <h3 class="modal-title" id="modal-title"></h3>
                <p class="modal-desc" id="modal-desc"></p>
            </div>
        </div>
    </div>

    <!-- Script Layer -->
    <script>
        // SafeStorage: In-memory fallback if third-party cookies or sandboxed iframes block localStorage
        const SafeStorage = {{
            _mem: {{}},
            getItem: function(key) {{
                try {{
                    return window.localStorage.getItem(key);
                }} catch (e) {{
                    return this._mem[key] !== undefined ? this._mem[key] : null;
                }}
            }},
            setItem: function(key, val) {{
                try {{
                    window.localStorage.setItem(key, String(val));
                }} catch (e) {{
                    this._mem[key] = String(val);
                }}
            }},
            removeItem: function(key) {{
                try {{
                    window.localStorage.removeItem(key);
                }} catch (e) {{
                    delete this._mem[key];
                }}
            }}
        }};

        let currentLang = 'fr';
        let currentMood = "✨ Pleine d'espoir";

        // Bilingual Translations
        const i18n = {{
            fr: {{
                title: "Mon Parcours FIV",
                subtitle: "Fatou ♥ · RMA of New York (East Side) & Gramercy Surgery Center",
                pin: "🔒 Code PIN · 1998",
                dr: "🩺 Dr. Coperman",
                pdf: "Imprimer / PDF",
                apk: "APK Android",
                oocytes: "Ovocytes Prélevés",
                mature: "Ovocytes Matures (MII)",
                fertilized: "Fécondés par ICSI",
                blastocysts: "Blastocystes Vitrifiés",
                protoTitle: "Protocole Médical FIV",
                badgeSept: "15 ÉTAPES TERMINÉES",
                badgeUp: "7 ÉTAPES GROSSESSE",
                tabSept: "Septembre 2026 (Stimulation - 15 étapes)",
                tabUp: "Octobre & Novembre (Grossesse & FET - 7 étapes)",
                galleryTitle: "Journal Clinique Illustré",
                gallerySub: "Sélectionnez une vignette pour visualiser les détails échographiques et embryonnaires.",
                notesTitle: "Notes personnelles de Fatou",
                notesDesc: "Notez vos ressentis quotidiens, questions pour le médecin ou pensées du jour. Vos notes sont sauvegardées automatiquement.",
                placeholder: "Chère Fatou, écrivez vos pensées du jour ici...",
                saved: "✓ Sauvegardé automatiquement",
                chkTitle: "Priorités & Préparatifs",
                footer: "Mon Parcours FIV · Fatou ♥ · RMA of New York (East Side) & Gramercy Surgery Center · Document confidentiel de suivi médical personnel · Édition Bureau / Desktop Brown"
            }},
            en: {{
                title: "My IVF Journey",
                subtitle: "Fatou ♥ · RMA of New York (East Side) & Gramercy Surgery Center",
                pin: "🔒 PIN Code · 1998",
                dr: "🩺 Dr. Coperman",
                pdf: "Print / PDF",
                apk: "Android APK",
                oocytes: "Oocytes Retrieved",
                mature: "Mature Oocytes (MII)",
                fertilized: "Fertilized by ICSI",
                blastocysts: "Vitrified Blastocysts",
                protoTitle: "IVF Medical Protocol",
                badgeSept: "15 STEPS COMPLETED",
                badgeUp: "7 PREGNANCY STEPS",
                tabSept: "September 2026 (Stimulation - 15 steps)",
                tabUp: "October & November (Pregnancy & FET - 7 steps)",
                galleryTitle: "Illustrated Clinical Journal",
                gallerySub: "Select a thumbnail to view ultrasound and embryology clinical insights.",
                notesTitle: "Fatou's Personal Notes",
                notesDesc: "Write down your daily feelings, doctor questions, or personal thoughts. Notes are automatically saved.",
                placeholder: "Dear Fatou, write your thoughts of the day here...",
                saved: "✓ Auto-saved",
                chkTitle: "Priorities & Preparations",
                footer: "My IVF Journey · Fatou ♥ · RMA of New York (East Side) & Gramercy Surgery Center · Confidential Medical Roadmap · Desktop Brown Edition"
            }}
        }};

        function switchLanguage(lang) {{
            currentLang = lang;
            document.getElementById('btn-fr').classList.toggle('active', lang === 'fr');
            document.getElementById('btn-en').classList.toggle('active', lang === 'en');
            const t = i18n[lang];

            document.getElementById('header-title').textContent = t.title;
            document.getElementById('header-subtitle').textContent = t.subtitle;
            document.getElementById('badge-pin').textContent = t.pin;
            document.getElementById('badge-dr').textContent = t.dr;
            document.getElementById('btn-pdf-text').textContent = t.pdf;
            document.getElementById('btn-apk-text').textContent = t.apk;
            document.getElementById('lbl-oocytes').textContent = t.oocytes;
            document.getElementById('lbl-mature').textContent = t.mature;
            document.getElementById('lbl-fertilized').textContent = t.fertilized;
            document.getElementById('lbl-blastocysts').textContent = t.blastocysts;
            document.getElementById('proto-panel-title').textContent = t.protoTitle;
            document.getElementById('tab-sept').textContent = t.tabSept;
            document.getElementById('tab-up').textContent = t.tabUp;
            document.getElementById('gallery-title').textContent = t.galleryTitle;
            document.getElementById('gallery-sub').textContent = t.gallerySub;
            document.getElementById('notes-card-title').textContent = t.notesTitle;
            document.getElementById('notes-card-desc').textContent = t.notesDesc;
            document.getElementById('notes-area').placeholder = t.placeholder;
            document.getElementById('chk-card-title').textContent = t.chkTitle;
            document.getElementById('footer-note').textContent = t.footer;

            const isSept = document.getElementById('tab-sept').classList.contains('active');
            document.getElementById('proto-badge').textContent = isSept ? t.badgeSept : t.badgeUp;
        }}

        // Timeline Tab Switch
        function switchTab(tab) {{
            const septTab = document.getElementById('tab-sept');
            const upTab = document.getElementById('tab-up');
            const septSteps = document.getElementById('september-steps');
            const upSteps = document.getElementById('upcoming-steps');
            const badge = document.getElementById('proto-badge');
            const t = i18n[currentLang];

            if (tab === 'sept') {{
                septTab.classList.add('active');
                upTab.classList.remove('active');
                septSteps.style.display = 'flex';
                upSteps.style.display = 'none';
                badge.textContent = t.badgeSept;
            }} else {{
                septTab.classList.remove('active');
                upTab.classList.add('active');
                septSteps.style.display = 'none';
                upSteps.style.display = 'flex';
                badge.textContent = t.badgeUp;
            }}
        }}

        // Checklist Checkmarks
        function toggleCheck(el, key) {{
            el.classList.toggle('checked');
            SafeStorage.setItem('brown_chk_' + key, el.classList.contains('checked') ? '1' : '0');
        }}

        // Notes and Moods
        function setMood(el, mood) {{
            currentMood = mood;
            document.querySelectorAll('#mood-box .mood-pill').forEach(b => b.classList.remove('active'));
            if (el) el.classList.add('active');
            SafeStorage.setItem('brown_mood', mood);
        }}

        const notesArea = document.getElementById('notes-area');
        if (notesArea) {{
            notesArea.addEventListener('input', () => {{
                SafeStorage.setItem('brown_draft', notesArea.value);
                const status = document.getElementById('notes-status');
                if (status) {{
                    const now = new Date();
                    const timeStr = now.toLocaleTimeString(currentLang === 'fr' ? 'fr-FR' : 'en-US', {{ hour: '2-digit', minute: '2-digit' }});
                    status.textContent = currentLang === 'fr' ? `✓ Sauvegardé (${{timeStr}})` : `✓ Saved (${{timeStr}})`;
                }}
            }});
        }}

        function insertDate() {{
            if (!notesArea) return;
            const now = new Date();
            const dateStr = now.toLocaleDateString(currentLang === 'fr' ? 'fr-FR' : 'en-US', {{ day: 'numeric', month: 'long', year: 'numeric' }});
            const stamp = `[${{dateStr}} · ${{currentMood}}] `;
            if (!notesArea.value.includes(stamp)) {{
                notesArea.value = stamp + notesArea.value;
                SafeStorage.setItem('brown_draft', notesArea.value);
                notesArea.focus();
            }}
        }}

        function clearDraft() {{
            if (!notesArea) return;
            if (notesArea.value.trim().length > 0) {{
                if (!confirm(currentLang === 'fr' ? 'Effacer le brouillon en cours ?' : 'Clear current draft?')) return;
            }}
            notesArea.value = '';
            SafeStorage.removeItem('brown_draft');
            document.getElementById('notes-status').textContent = currentLang === 'fr' ? 'Brouillon effacé' : 'Draft cleared';
        }}

        function getNotesFeed() {{
            try {{
                const raw = SafeStorage.getItem('brown_feed');
                if (raw) return JSON.parse(raw);
            }} catch (e) {{}}
            return [
                {{
                    id: 'n1',
                    date: '26 octobre 2026 · 14 h 30',
                    mood: "✨ Pleine d'espoir",
                    text: 'En attente douce des résultats PGT-A. Chaque jour nous rapproche de notre futur bébé. Rester sereine, respirer profondément, et faire confiance au travail remarquable du laboratoire RMA.'
                }},
                {{
                    id: 'n2',
                    date: '24 octobre 2026 · 09 h 15',
                    mood: '🌸 Sereine',
                    text: 'Rendez-vous confirmé à 08h00 à RMA East Side pour l\\'hystérosonographie SIS du 28 octobre avec le Dr Coperman. Penser à prendre une bouteille d\\'eau.'
                }}
            ];
        }}

        function saveToJournal() {{
            if (!notesArea) return;
            const text = notesArea.value.trim();
            if (!text) {{
                alert(currentLang === 'fr' ? 'Veuillez écrire quelques mots avant d\\\'ajouter au journal.' : 'Please write a few words before adding to journal.');
                notesArea.focus();
                return;
            }}
            const now = new Date();
            const dateStr = now.toLocaleDateString(currentLang === 'fr' ? 'fr-FR' : 'en-US', {{ day: 'numeric', month: 'long', year: 'numeric' }});
            const timeStr = now.toLocaleTimeString(currentLang === 'fr' ? 'fr-FR' : 'en-US', {{ hour: '2-digit', minute: '2-digit' }});

            const newEntry = {{
                id: 'n_' + Date.now(),
                date: `${{dateStr}} · ${{timeStr}}`,
                mood: currentMood,
                text: text
            }};

            const feed = getNotesFeed();
            feed.unshift(newEntry);
            SafeStorage.setItem('brown_feed', JSON.stringify(feed));

            notesArea.value = '';
            SafeStorage.removeItem('brown_draft');
            renderNotesFeed();

            document.getElementById('notes-status').textContent = currentLang === 'fr' ? '✓ Ajouté au journal !' : '✓ Added to journal!';
        }}

        function deleteNote(id) {{
            if (!confirm(currentLang === 'fr' ? 'Supprimer cette pensée ?' : 'Delete this note?')) return;
            let feed = getNotesFeed();
            feed = feed.filter(n => n.id !== id);
            SafeStorage.setItem('brown_feed', JSON.stringify(feed));
            renderNotesFeed();
        }}

        function renderNotesFeed() {{
            const container = document.getElementById('saved-notes-list');
            if (!container) return;
            const feed = getNotesFeed();
            if (feed.length === 0) {{
                container.innerHTML = `<div style="font-size:11px; color:#8C7B72; font-style:italic; padding:6px 0;">${{currentLang === 'fr' ? 'Aucune pensée enregistrée.' : 'No saved notes yet.'}}</div>`;
                return;
            }}
            container.innerHTML = feed.map(item => `
                <div class="saved-note-bubble">
                    <div class="bubble-head">
                        <span>📅 ${{item.date}} · <strong>${{item.mood || ''}}</strong></span>
                        <button type="button" class="bubble-del" onclick="deleteNote('${{item.id}}')" title="Supprimer">✕</button>
                    </div>
                    <div class="bubble-text">${{escapeHtml(item.text)}}</div>
                </div>
            `).join('');
        }}

        function escapeHtml(s) {{
            return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
        }}

        // Lightbox Details
        const galleryData = {{
            pgta: {{
                src: "{svgs['journal_pgta.svg']}",
                stageFr: "Étape 1 · 27 Octobre 2026",
                stageEn: "Step 1 · October 27, 2026",
                titleFr: "Résultats PGT-A · 8 Blastocystes",
                titleEn: "PGT-A Screening Results · 8 Blastocysts",
                descFr: "Illustration clinique au microscope révélant le trophoectoderme régulier et la masse cellulaire interne d'un blastocyste à vitalité optimale avant transfert.",
                descEn: "Clinical microscopy illustration showing regular trophectoderm and inner cell mass of an optimal viability blastocyst prior to transfer."
            }},
            sis: {{
                src: "{svgs['journal_sis.svg']}",
                stageFr: "Étape 2 · 28 Octobre 2026 (08h00)",
                stageEn: "Step 2 · October 28, 2026 (08:00 AM)",
                titleFr: "Hystérosonographie SIS 3D · RMA East Side",
                titleEn: "3D Saline Infusion Sonogram (SIS) · RMA East Side",
                descFr: "Reconstitution échographique haute résolution de la cavité utérine montrant un endomètre trilaminaire réceptif et parfaitement lisse.",
                descEn: "High-resolution ultrasound reconstruction of the uterine cavity showing a receptive, perfectly smooth trilaminar endometrium."
            }},
            transfer: {{
                src: "{svgs['journal_transfer.svg']}",
                stageFr: "Étape 4 · 31 Octobre 2026 (Midi)",
                stageEn: "Step 4 · October 31, 2026 (Noon)",
                titleFr: "Transfert FET · Implantation Guidée",
                titleEn: "FET Transfer · Ultrasound-Guided Implantation",
                descFr: "Guidage échographique abdominal révélant le dépôt précis du cathéter souple au centre du tiers supérieur de la cavité utérine.",
                descEn: "Abdominal ultrasound guidance showing delicate placement of the soft catheter in the upper third of the uterine cavity."
            }},
            betahcg: {{
                src: "{svgs['journal_betahcg.svg']}",
                stageFr: "Étape 5 · 08 Novembre 2026 (07h30)",
                stageEn: "Step 5 · November 8, 2026 (07:30 AM)",
                titleFr: "Prise de Sang Bêta-hCG · Confirmation Grossesse",
                titleEn: "Beta-hCG Blood Test · Pregnancy Confirmation",
                descFr: "Dosage sérique officiel révélant la présence de l'hormone chorionique gonadotrope humaine (hCG), attestant d'une nidation active.",
                descEn: "Official serum test confirming human chorionic gonadotropin (hCG), confirming active implantation and early pregnancy."
            }},
            ultrasound: {{
                src: "{svgs['journal_ultrasound.svg']}",
                stageFr: "Étape 7 · 22 Novembre 2026 (09h30)",
                stageEn: "Step 7 · November 22, 2026 (09:30 AM)",
                titleFr: "1ère Échographie · 6-7 SA · Battement Cardiaque",
                titleEn: "1st Ultrasound · 6-7 Weeks · Fetal Heartbeat",
                descFr: "Visuel échographique obstétrical révélant le sac gestationnel intra-utérin avec l'écho embryonnaire et son activité cardiaque rythmée.",
                descEn: "Obstetric ultrasound revealing the intrauterine gestational sac with embryonic pole and rhythmic fetal cardiac activity."
            }}
        }};

        function openLightbox(key) {{
            const item = galleryData[key];
            if (!item) return;
            document.getElementById('modal-img').src = item.src;
            document.getElementById('modal-stage').textContent = currentLang === 'fr' ? item.stageFr : item.stageEn;
            document.getElementById('modal-title').textContent = currentLang === 'fr' ? item.titleFr : item.titleEn;
            document.getElementById('modal-desc').textContent = currentLang === 'fr' ? item.descFr : item.descEn;
            document.getElementById('lightbox-modal').classList.add('open');
        }}

        function closeLightbox(e) {{
            document.getElementById('lightbox-modal').classList.remove('open');
        }}

        // Initialize state on load
        function initApp() {{
            // Restore checklist items
            ['chk_1', 'chk_2', 'chk_3', 'chk_4', 'chk_5', 'chk_6'].forEach(id => {{
                const s = SafeStorage.getItem('brown_chk_' + id);
                if (s !== null) {{
                    const row = document.querySelector(`.check-item[onclick*="${{id}}"]`);
                    if (row) {{
                        if (s === '1') row.classList.add('checked');
                        else row.classList.remove('checked');
                    }}
                }}
            }});

            // Restore draft and mood
            const draft = SafeStorage.getItem('brown_draft');
            if (draft && notesArea) notesArea.value = draft;

            const savedMood = SafeStorage.getItem('brown_mood');
            if (savedMood) {{
                currentMood = savedMood;
                document.querySelectorAll('#mood-box .mood-pill').forEach(b => {{
                    if (b.textContent.trim() === savedMood) b.classList.add('active');
                    else b.classList.remove('active');
                }});
            }}

            renderNotesFeed();
        }}

        if (document.readyState === 'loading') {{
            document.addEventListener('DOMContentLoaded', initApp);
        }} else {{
            initApp();
        }}
    </script>
</body>
</html>'''

with open('public/desktop-brown.html', 'w', encoding='utf-8') as f:
    f.write(html_content)

print(f'desktop-brown.html successfully written! ({len(html_content)} bytes)')
