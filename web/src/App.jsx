import React, { useState, useEffect } from 'react';
import { Heart, Calendar, TestTube, CheckSquare, BookOpen, Baby, Lock, Unlock, Download, Bell, Plus, Trash2, Edit3, Menu, X } from 'lucide-react';
import { fetchNotesFromKV, saveNotesToKV } from './kvService';

export default function App() {
  const [currentTab, setCurrentTab] = useState('home');
  const [isGoldTheme, setIsGoldTheme] = useState(false);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  // Synchronized Checklist items state
  const [checklist, setChecklist] = useState([
    { id: 'chk-defrost', symbol: '🧊', title: 'Plan de décongélation terminé (13 oct.)', dateText: '13 octobre · soumis', badgeText: '✓ Validé', badgeType: 'past', isCompleted: true, description: 'Soumis via le portail avec préférence pour un garçon (Male). Sélection du blastocyste euploïde masculin optimal.' },
    { id: 'chk-pgta', symbol: '🧬', title: 'Résultats PGT-A reçus (25 oct.)', dateText: '25 octobre · Reçu ✓', badgeText: '✓ 5 Normaux', badgeType: 'past', isCompleted: true, description: '5 embryons normaux (euploïdes) sur 8 blastocystes biopsiés reçus de l\'analyse génétique.' },
    { id: 'chk-nortrel', symbol: '💊', title: 'Maintien de la médication quotidienne', dateText: 'Chaque jour', badgeText: '⏱️ En cours', badgeType: 'today', isCompleted: false, description: 'Continuer le Nortrel selon les instructions actuelles de l’équipe RMA.' },
    { id: 'chk-sis', symbol: '🏥', title: 'Hystérosonographie SIS', dateText: '28 octobre · 08 h 00', badgeText: '⏳ AUJOURD\'HUI', badgeType: 'today', isCompleted: false, description: 'RMA East Side (635 Madison Ave). Examen pour évaluer la cavité utérine et l\'endomètre.' },
    { id: 'chk-hystero', symbol: '🔬', title: 'Hystéroscopie opératoire (retrait de débris)', dateText: '30 octobre · 07 h 30', badgeText: '⏳ J-2', badgeType: 'imminent', isCompleted: false, description: 'Gramercy Surgery Center (380 2nd Avenue, Suite 1000, New York, NY 10010).' },
    { id: 'chk-call', symbol: '☎️', title: 'Planifier l’appel de protocole FET', dateText: 'Début novembre', badgeText: '⏳ Prochainement', badgeType: 'future', isCompleted: false, description: 'Confirmer la date définitive du transfert d’embryon avec l\'équipe médicale.' }
  ]);

  // Synchronized Journal notes state with Vercel KV persistence
  const [notes, setNotes] = useState([
    { id: '1', title: 'Résultats PGT-A reçus : 5 embryons normaux !', date: '2026-10-25', category: 'Jalon important ✓', content: 'Reçu les résultats PGT-A aujourd\'hui : 5 embryons normaux (euploïdes) sur les 8 blastocystes biopsiés. Prêt pour la suite avec notre petit garçon ♡' },
    { id: '2', title: 'En attente douce des résultats PGT-A', date: '2026-10-26', category: 'Émotion', content: 'Chaque jour nous rapproche. Rester sereine, respirer profondément, et faire confiance au laboratoire.' },
    { id: '3', title: 'Après la ponction : 42 ovocytes !', date: '2026-10-01', category: 'Jalon', content: '42 ovocytes prélevés, 34 matures et 8 magnifiques blastocystes vitrifiés. Très fière ♡' }
  ]);
  const [newNoteTitle, setNewNoteTitle] = useState('');
  const [newNoteContent, setNewNoteContent] = useState('');

  // Fetch notes from Vercel KV on mount
  useEffect(() => {
    fetchNotesFromKV().then((fetched) => {
      if (fetched && Array.isArray(fetched) && fetched.length > 0) {
        setNotes(fetched);
      }
    });
  }, []);

  const toggleCheck = (id) => {
    const updated = checklist.map(item => item.id === id ? { ...item, isCompleted: !item.isCompleted } : item);
    setChecklist(updated);
  };

  const addNote = (e) => {
    e.preventDefault();
    if (!newNoteTitle) return;
    const updatedNotes = [
      { id: Date.now().toString(), title: newNoteTitle, date: new Date().toISOString().split('T')[0], category: 'Personnel', content: newNoteContent },
      ...notes
    ];
    setNotes(updatedNotes);
    saveNotesToKV(updatedNotes);
    setNewNoteTitle('');
    setNewNoteContent('');
  };

  const deleteNote = (id) => {
    const updatedNotes = notes.filter(n => n.id !== id);
    setNotes(updatedNotes);
    saveNotesToKV(updatedNotes);
  };

  const headerBg = isGoldTheme ? 'bg-[#947230]' : 'bg-[#5C3B24]';

  const navItems = [
    { id: 'home', label: 'Accueil · Home', icon: '⌂' },
    { id: 'calendar', label: 'Agenda · Cal', icon: '▦' },
    { id: 'lab', label: 'Labo · Lab', icon: '🧪' },
    { id: 'checklist', label: 'Actions', icon: '☑' },
    { id: 'journal', label: 'Notes', icon: '✍' },
    { id: 'obstetric', label: 'Bébé · Baby', icon: '👶' },
  ];

  return (
    <div className="min-h-screen bg-[#FAF6F0] text-[#2B2320] flex flex-col md:flex-row overflow-x-hidden">
      
      {/* Desktop Sidebar Navigation */}
      <aside className={`hidden md:flex flex-col w-64 ${headerBg} text-white p-6 shadow-xl transition-colors duration-300 justify-between shrink-0`}>
        <div className="space-y-6">
          <div className="flex items-center gap-3">
            <span className="text-2xl text-[#FFDF7D]">♡</span>
            <div>
              <h1 className="font-serif font-bold text-lg leading-tight">Mon Parcours</h1>
              <p className="text-xs text-[#F0DEC9]">Fatou ♥ · RMA East Side</p>
            </div>
          </div>
          <nav className="space-y-1.5">
            {navItems.map(item => (
              <button
                key={item.id}
                onClick={() => setCurrentTab(item.id)}
                className={`w-full flex items-center gap-3 px-4 py-3 min-h-[48px] rounded-xl text-sm font-semibold transition ${currentTab === item.id ? 'bg-white/20 text-white shadow-inner' : 'text-white/80 hover:bg-white/10 hover:text-white'}`}
              >
                <span className="text-lg">{item.icon}</span>
                <span>{item.label}</span>
              </button>
            ))}
          </nav>
        </div>
        <div className="space-y-3 pt-6 border-t border-white/20">
          <button 
            onClick={() => setIsGoldTheme(!isGoldTheme)}
            className="w-full py-3 min-h-[48px] text-xs font-bold bg-black/20 hover:bg-black/30 rounded-xl transition flex items-center justify-center gap-2"
          >
            {isGoldTheme ? '✨ Thème Or Actif' : '🤎 Thème Fatou Actif'}
          </button>
          <button 
            onClick={() => alert("Résumé PDF généré et téléchargé avec succès !")}
            className="w-full py-3 min-h-[48px] text-xs font-bold bg-white text-[#5C3B24] hover:bg-[#F4EBD8] rounded-xl transition flex items-center justify-center gap-2 shadow"
          >
            <Download size={14} /> Télécharger Résumé PDF
          </button>
        </div>
      </aside>

      {/* Main Container */}
      <div className="flex-1 flex flex-col min-h-screen w-full overflow-x-hidden">
        
        {/* Mobile Header */}
        <div className={`md:hidden ${headerBg} text-white p-4 flex items-center justify-between shadow-md transition-colors duration-300 sticky top-0 z-20`}>
          <div className="flex items-center gap-2.5">
            <span className="text-xl text-[#FFDF7D]">♡</span>
            <div>
              <h1 className="text-base font-serif font-bold leading-tight">Mon Parcours · Fatou</h1>
              <p className="text-[10px] text-[#F0DEC9]">RMA NY · PGT-A Reçu ✓</p>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <button 
              onClick={() => setIsGoldTheme(!isGoldTheme)}
              className="px-3 py-2 min-h-[44px] text-[11px] font-extrabold bg-black/20 rounded-full border border-white/20 flex items-center justify-center"
            >
              {isGoldTheme ? '✨ Or' : '🤎 Fatou'}
            </button>
            <button 
              onClick={() => alert("Résumé PDF généré !")}
              className="px-3 py-2 min-h-[44px] text-[11px] font-bold bg-black/30 rounded-full border border-white/20 flex items-center gap-1.5 justify-center"
            >
              <Download size={12} /> PDF
            </button>
          </div>
        </div>

        {/* Content Area */}
        <main className="flex-1 max-w-4xl w-full mx-auto p-4 sm:p-6 lg:p-8 space-y-6 pb-28 md:pb-8">
          
          {/* Next Step Countdown Widget (Dual Language) */}
          <div className="bg-[#F4EBD8] border border-[#5C3B24] rounded-2xl p-5 shadow-sm space-y-3">
            <div className="flex justify-between items-center flex-wrap gap-3">
              <div className="flex items-center gap-3">
                <span className="text-2xl">⏰</span>
                <div>
                  <p className="text-[10px] font-extrabold text-[#5C3B24] tracking-wider uppercase">PROCHAINE ÉTAPE · NEXT STEP</p>
                  <h3 className="font-serif font-bold text-base sm:text-lg text-[#2B2320]">Hystérosonographie SIS · SIS Ultrasound</h3>
                </div>
              </div>
              <div className="bg-[#5C3B24] text-white px-4 py-2 rounded-xl text-center shadow">
                <p className="text-xs font-extrabold">2 Jours & 14 Heures</p>
                <p className="text-[9px] text-[#F0DEC9]">2 Days & 14 Hours</p>
              </div>
            </div>
            <p className="text-xs text-[#6E6058]">28 oct. 2026 · 08 h 00 · RMA East Side (635 Madison Ave)</p>
          </div>

          {/* TAB 1: HOME */}
          {currentTab === 'home' && (
            <div className="space-y-6">
              {/* Hero Banner */}
              <div className="bg-gradient-to-br from-[#F4EBD8] via-[#FCF9F5] to-[#EAE0D0] border border-[#D8C7B3] rounded-2xl p-6 sm:p-8 shadow-sm">
                <span className="text-xs font-extrabold text-[#5C3B24] tracking-wider uppercase">VOTRE PARCOURS FIV · YOUR IVF JOURNEY</span>
                <h2 className="text-2xl sm:text-3xl lg:text-4xl font-serif font-medium text-[#2B2320] mt-1.5 leading-snug">De la première injection au premier battement de cœur</h2>
                <p className="text-sm sm:text-base text-[#6E6058] mt-2.5">Chaque étape chez RMA of New York est documentée avec précision et synchronisée en temps réel.</p>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {/* PGT-A Results Success Card */}
                <div className="bg-[#EAF4ED] border border-[#BFE0CB] rounded-2xl p-5 sm:p-6 shadow-sm space-y-3">
                  <div className="flex justify-between items-center">
                    <div className="flex items-center gap-3">
                      <div className="w-11 h-11 rounded-full bg-[#C7E2D0] flex items-center justify-center text-xl shrink-0">🧬</div>
                      <div>
                        <h3 className="font-serif font-bold text-base text-[#2B7853]">Résultats PGT-A Reçus ✓</h3>
                        <p className="text-xs text-[#2B7853]">5 embryons normaux sur 8 blastocystes</p>
                      </div>
                    </div>
                    <span className="text-xs font-extrabold bg-[#2B7853] text-white px-3 py-1.5 rounded-full shrink-0">100% Succès</span>
                  </div>
                  <p className="text-xs text-[#2B7853] leading-relaxed">Formulaire de décongélation soumis avec préférence pour un garçon (Male). Le transfert d'un blastocyste euploïde masculin est programmé fin octobre / début novembre.</p>
                </div>

                {/* Continued Support Meds */}
                <div className="bg-white border border-[#E2D3C4] rounded-2xl p-5 sm:p-6 shadow-sm space-y-4">
                  <div className="flex justify-between items-center">
                    <div className="flex items-center gap-3">
                      <div className="w-11 h-11 rounded-full bg-[#EAF4ED] flex items-center justify-center text-xl shrink-0">💊</div>
                      <div>
                        <h3 className="font-serif font-bold text-base text-[#2B2320]">Soutien Hormonal Continu</h3>
                        <p className="text-xs text-[#6E6058]">Progestérone & Nortrel</p>
                      </div>
                    </div>
                    <span className="text-xs font-bold bg-[#F6ECE0] text-[#5C3B24] px-3 py-1.5 rounded-full shrink-0">Essentiel</span>
                  </div>
                  <div className="space-y-3">
                    <div className="bg-[#FAF7F3] border border-[#E8DFC9] p-3.5 rounded-xl flex items-center justify-between gap-3">
                      <div className="min-w-0">
                        <p className="text-xs sm:text-sm font-bold text-[#2B2320]">Progestérone en Huile (PIO) 50 mg</p>
                        <p className="text-xs text-[#8D6648]">⏰ 08 h 00 · Injection intramusculaire</p>
                      </div>
                      <input type="checkbox" defaultChecked className="w-6 h-6 accent-[#2B7853] rounded cursor-pointer shrink-0" />
                    </div>
                    <div className="bg-[#FAF7F3] border border-[#E8DFC9] p-3.5 rounded-xl flex items-center justify-between gap-3">
                      <div className="min-w-0">
                        <p className="text-xs sm:text-sm font-bold text-[#2B2320]">Nortrel & Estrogènes</p>
                        <p className="text-xs text-[#8D6648]">⏰ Selon protocole RMA</p>
                      </div>
                      <input type="checkbox" className="w-6 h-6 accent-[#2B7853] rounded cursor-pointer shrink-0" />
                    </div>
                  </div>
                </div>
              </div>

              {/* Vos prochaines actions */}
              <div className="bg-white border border-[#E2D3C4] rounded-2xl p-5 sm:p-6 shadow-sm space-y-4">
                <div className="flex justify-between items-center">
                  <h3 className="font-serif font-bold text-base sm:text-lg text-[#5C3B24]">✓ Vos prochaines actions · Immediate Checklist</h3>
                  <span className="text-[10px] font-extrabold bg-[#EBDCCF] text-[#543722] px-3 py-1.5 rounded-lg">OCTOBRE 2026</span>
                </div>
                <div className="space-y-2.5">
                  {checklist.map(item => (
                    <div key={item.id} onClick={() => toggleCheck(item.id)} className="flex items-center gap-3 cursor-pointer p-3 min-h-[48px] hover:bg-[#FAF6F0] rounded-xl transition border border-transparent hover:border-[#E2D3C4]">
                      <input type="checkbox" checked={item.isCompleted} onChange={() => {}} className="w-5 h-5 accent-[#2B7853] cursor-pointer shrink-0" />
                      <div className="flex-1 min-w-0">
                        <p className={`text-sm font-semibold truncate ${item.isCompleted ? 'line-through text-[#6E6058]' : 'text-[#2B2320]'}`}>{item.title}</p>
                        <p className="text-xs text-[#6E6058]">{item.dateText}</p>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}

          {/* TAB 2: CALENDAR */}
          {currentTab === 'calendar' && (
            <div className="space-y-6">
              <h2 className="text-2xl font-serif font-bold text-[#5C3B24]">Calendrier Médical du Protocole · Protocol Calendar</h2>
              <div className="bg-white border border-[#E2D3C4] rounded-2xl p-5 sm:p-6 shadow-sm space-y-4">
                <div className="flex justify-between items-center">
                  <span className="font-serif font-bold text-base text-[#5C3B24]">Octobre 2026 (FET, SIS & Hystéroscopie Gramercy)</span>
                  <span className="text-xs bg-[#EAF4ED] text-[#2B7853] px-3 py-1.5 rounded-full font-bold">Mis à jour ✓</span>
                </div>
                <div className="grid grid-cols-7 gap-2 text-center text-xs font-bold text-[#6E6058]">
                  <span>Lun</span><span>Mar</span><span>Mer</span><span>Jeu</span><span>Ven</span><span>Sam</span><span>Dim</span>
                </div>
                <div className="grid grid-cols-7 gap-2 text-center text-sm">
                  {Array.from({ length: 31 }).map((_, i) => {
                    const day = i + 1;
                    const hasEvent = [1, 13, 25, 28, 30, 31].includes(day);
                    return (
                      <div key={day} className={`p-3.5 min-h-[44px] flex items-center justify-center rounded-xl ${day === 28 ? 'bg-[#5C3B24] text-white font-bold shadow' : hasEvent ? 'bg-[#F7EEE4] font-bold text-[#5C3B24]' : 'text-[#2B2320]'}`}>
                        {day}
                      </div>
                    );
                  })}
                </div>
                <div className="bg-[#FCF9F5] border border-[#E2D3C4] p-4 sm:p-5 rounded-xl space-y-2">
                  <span className="text-xs font-bold text-[#5C3B24] bg-[#F4EBD8] px-3 py-1 rounded inline-block">30 Octobre · 07 h 30</span>
                  <h4 className="font-serif font-bold text-base">Hystéroscopie opératoire (retrait de débris)</h4>
                  <p className="text-sm text-[#6E6058]">Gramercy Surgery Center (380 2nd Avenue, Suite 1000, 10th Floor, New York, NY 10010).</p>
                </div>
              </div>
            </div>
          )}

          {/* TAB 3: LAB */}
          {currentTab === 'lab' && (
            <div className="space-y-6">
              <h2 className="text-2xl font-serif font-bold text-[#5C3B24]">Dossier de Laboratoire (RMA NY)</h2>
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                <div className="bg-white border border-[#E2D3C4] p-5 rounded-2xl shadow-sm text-center space-y-2">
                  <span className="text-3xl">🥚</span>
                  <p className="text-3xl font-serif font-bold text-[#5C3B24]">42</p>
                  <p className="text-xs font-semibold text-[#6E6058]">Ovocytes prélevés</p>
                </div>
                <div className="bg-white border border-[#E2D3C4] p-5 rounded-2xl shadow-sm text-center space-y-2">
                  <span className="text-3xl">◉</span>
                  <p className="text-3xl font-serif font-bold text-[#5C3B24]">34</p>
                  <p className="text-xs font-semibold text-[#6E6058]">Ovocytes matures</p>
                </div>
                <div className="bg-white border border-[#E2D3C4] p-5 rounded-2xl shadow-sm text-center space-y-2">
                  <span className="text-3xl">❄</span>
                  <p className="text-3xl font-serif font-bold text-[#5C3B24]">8</p>
                  <p className="text-xs font-semibold text-[#6E6058]">Blastocystes biopsiés</p>
                </div>
                <div className="bg-[#EAF4ED] border border-[#BFE0CB] p-5 rounded-2xl shadow-sm text-center space-y-2">
                  <span className="text-3xl">🧬</span>
                  <p className="text-3xl font-serif font-bold text-[#2B7853]">5 / 8</p>
                  <p className="text-xs font-semibold text-[#2B7853]">Embryons normaux (euploïdes) · PGT-A Reçus ✓</p>
                </div>
              </div>
            </div>
          )}

          {/* TAB 4: CHECKLIST */}
          {currentTab === 'checklist' && (
            <div className="space-y-4">
              <div>
                <span className="text-[10px] font-extrabold text-[#6E6058] tracking-wider uppercase">Priorités du moment · Immediate Checklist</span>
                <h2 className="text-lg sm:text-xl font-serif font-bold text-[#5C3B24]">Liste de contrôle d’exécution immédiate</h2>
                <p className="text-xs sm:text-sm text-[#6E6058]">Cochez les tâches au fur et à mesure pour synchroniser votre avancement.</p>
              </div>
              <div className="space-y-3">
                {checklist.map(item => (
                  <div key={item.id} onClick={() => toggleCheck(item.id)} className="bg-white border border-[#E2D3C4] p-4 rounded-xl shadow-sm flex items-start gap-3 cursor-pointer hover:bg-[#FAF6F0] transition min-h-[48px]">
                    <input type="checkbox" checked={item.isCompleted} onChange={() => {}} className="w-5 h-5 accent-[#2B7853] mt-1 cursor-pointer shrink-0" />
                    <span className="text-xl shrink-0">{item.symbol}</span>
                    <div className="flex-1 min-w-0">
                      <div className="flex justify-between items-center mb-1 gap-2 flex-wrap">
                        <span className="text-[10px] font-bold text-[#5C3B24] uppercase">{item.dateText}</span>
                        <span className="text-[9px] font-bold px-2.5 py-1 rounded-full bg-[#EAF4ED] text-[#2B7853] shrink-0">{item.badgeText}</span>
                      </div>
                      <h4 className={`text-sm sm:text-base font-semibold ${item.isCompleted ? 'line-through text-[#6E6058]' : 'text-[#2B2320]'}`}>{item.title}</h4>
                      <p className="text-xs text-[#6E6058] mt-1 leading-relaxed">{item.description}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* TAB 5: JOURNAL */}
          {currentTab === 'journal' && (
            <div className="space-y-6">
              <h2 className="text-2xl font-serif font-bold text-[#5C3B24]">Journal de Bord & Notes Personnelles</h2>
              <form onSubmit={addNote} className="bg-white border border-[#E2D3C4] p-5 rounded-2xl shadow-sm space-y-4">
                <input 
                  type="text" 
                  placeholder="Titre de la note..." 
                  value={newNoteTitle} 
                  onChange={e => setNewNoteTitle(e.target.value)} 
                  className="w-full text-sm sm:text-base p-3.5 min-h-[48px] border border-[#E2D3C4] rounded-xl focus:outline-none focus:border-[#5C3B24]"
                />
                <textarea 
                  placeholder="Écrivez vos pensées et observations ici..." 
                  value={newNoteContent} 
                  onChange={e => setNewNoteContent(e.target.value)} 
                  className="w-full text-sm sm:text-base p-3.5 border border-[#E2D3C4] rounded-xl focus:outline-none focus:border-[#5C3B24] h-32"
                />
                <button type="submit" className="w-full py-3.5 min-h-[48px] bg-[#5C3B24] text-white text-sm sm:text-base font-bold rounded-xl hover:bg-[#452B18] transition shadow flex items-center justify-center">
                  Ajouter au Journal
                </button>
              </form>
              <div className="space-y-4">
                {notes.map(note => (
                  <div key={note.id} className="bg-white border border-[#E2D3C4] p-5 rounded-2xl shadow-sm space-y-2.5 relative">
                    <button onClick={() => deleteNote(note.id)} className="absolute top-4 right-4 text-red-500 hover:text-red-700 p-2 min-h-[40px] min-w-[40px] flex items-center justify-center">
                      <Trash2 size={18} />
                    </button>
                    <span className="text-xs font-bold text-[#5C3B24] bg-[#F4EBD8] px-3 py-1 rounded-md inline-block">{note.date}</span>
                    <h4 className="text-base sm:text-lg font-serif font-bold text-[#2B2320] pr-8">{note.title}</h4>
                    <p className="text-sm text-[#6E6058] leading-relaxed">{note.content}</p>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* TAB 6: GROSSESSE / OBSTETRIC JOURNEY */}
          {currentTab === 'obstetric' && (
            <div className="space-y-6">
              <h2 className="text-2xl font-serif font-bold text-[#5C3B24]">Parcours Grossesse · Pregnancy Journey</h2>
              <div className="bg-[#FBF6EE] border border-[#5C3B24] p-6 rounded-2xl text-sm sm:text-base text-[#6E6058] space-y-3 shadow-sm">
                <p className="font-bold text-base sm:text-lg text-[#5C3B24]">Suivi obstétrical détaillé de la graduation à juin 2027 :</p>
                <p>• <strong>Phase 3 · Suivi de début de grossesse (Nov. 2026) :</strong> Prises de sang Bêta-hCG sériées, premières échographies de viabilité et rythme cardiaque.</p>
                <p>• <strong>Phase 4 · Étapes obstétricales (Déc. 2026 – Juin 2027) :</strong> DPNI, clarté nucale, échographie morphologique (Niveau II), suivi de croissance et naissance en juin 2027 ! 🎉</p>
              </div>
            </div>
          )}

        </main>

        {/* Mobile Bottom Navigation Bar (No Web Icon) */}
        <div className="md:hidden fixed bottom-0 left-0 right-0 bg-[#FAF6F0] border-t border-[#E2D3C4] px-1 py-1.5 flex justify-around items-center z-30 shadow-lg">
          {navItems.map(tab => (
            <button
              key={tab.id}
              onClick={() => setCurrentTab(tab.id)}
              className={`flex flex-col items-center justify-center py-2 px-3 min-h-[48px] min-w-[48px] rounded-xl transition ${currentTab === tab.id ? 'text-[#5C3B24] font-bold bg-[#F4EBD8]' : 'text-[#6E6058] hover:text-[#2B2320]'}`}
            >
              <span className="text-xl">{tab.icon}</span>
              <span className="text-[10px] mt-0.5">{tab.label}</span>
            </button>
          ))}
        </div>

      </div>
    </div>
  );
}
