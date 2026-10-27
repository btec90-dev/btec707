import React, { useState } from 'react';
import { Heart, Calendar, TestTube, CheckSquare, BookOpen, Baby, Lock, Unlock, Download, Bell, Plus, Trash2, Edit3, Menu, X } from 'lucide-react';

export default function App() {
  const [currentTab, setCurrentTab] = useState('home');
  const [isGoldTheme, setIsGoldTheme] = useState(false);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  // Checklist items state
  const [checklist, setChecklist] = useState([
    { id: 'chk-defrost', symbol: '🧊', title: 'Plan de décongélation terminé', dateText: '13 octobre · soumis', badgeText: '✓ Validé', badgeType: 'past', isCompleted: true, description: 'Formulaire indiquant une préférence pour un garçon (« Male »). Sous réserve des résultats PGT-A.' },
    { id: 'chk-nortrel', symbol: '💊', title: 'Maintien de la médication quotidienne', dateText: 'Chaque jour', badgeText: '⏱️ En cours', badgeType: 'today', isCompleted: false, description: 'Continuer le Nortrel selon les instructions actuelles de l’équipe RMA.' },
    { id: 'chk-sis', symbol: '🏥', title: 'Hystérosonographie SIS', dateText: '28 octobre · 08 h 00', badgeText: '⏳ AUJOURD\'HUI', badgeType: 'today', isCompleted: false, description: 'RMA East Side. Examen pour évaluer la cavité utérine et l\'endomètre.' },
    { id: 'chk-hystero', symbol: '🔬', title: 'Hystéroscopie opératoire', dateText: '30 octobre · 07 h 30', badgeText: '⏳ J-2', badgeType: 'imminent', isCompleted: false, description: 'Gramercy Surgery Center. Intervention pour préparer une cavité utérine optimale avant le FET.' },
    { id: 'chk-pgta', symbol: '🧬', title: 'Surveiller les résultats PGT-A', dateText: '23–28 octobre', badgeText: '⏳ En cours', badgeType: 'today', isCompleted: false, description: 'Analyse chromosomique des 8 blastocystes vitrifiés.' }
  ]);

  // Journal notes state
  const [notes, setNotes] = useState([
    { id: '1', title: 'En attente douce des résultats PGT-A', date: '2026-10-26', category: 'Émotion', content: 'Chaque jour nous rapproche. Rester sereine, respirer profondément, et faire confiance au laboratoire.' },
    { id: '2', title: 'Après la ponction : 42 ovocytes !', date: '2026-10-01', category: 'Jalon', content: '42 ovocytes prélevés, 34 matures et 8 magnifiques blastocystes vitrifiés. Très fière ♡' }
  ]);
  const [newNoteTitle, setNewNoteTitle] = useState('');
  const [newNoteContent, setNewNoteContent] = useState('');

  const toggleCheck = (id) => {
    setChecklist(checklist.map(item => item.id === id ? { ...item, isCompleted: !item.isCompleted } : item));
  };

  const addNote = (e) => {
    e.preventDefault();
    if (!newNoteTitle) return;
    setNotes([{ id: Date.now().toString(), title: newNoteTitle, date: new Date().toISOString().split('T')[0], category: 'Personnel', content: newNoteContent }, ...notes]);
    setNewNoteTitle('');
    setNewNoteContent('');
  };

  const deleteNote = (id) => {
    setNotes(notes.filter(n => n.id !== id));
  };

  const headerBg = isGoldTheme ? 'bg-[#947230]' : 'bg-[#5C3B24]';

  const navItems = [
    { id: 'home', label: 'Accueil', icon: '⌂' },
    { id: 'calendar', label: 'Agenda', icon: '▦' },
    { id: 'lab', label: 'Labo', icon: '🧪' },
    { id: 'checklist', label: 'Actions', icon: '☑' },
    { id: 'journal', label: 'Notes', icon: '✍' },
    { id: 'obstetric', label: 'Bébé', icon: '👶' },
  ];

  return (
    <div className="min-h-screen bg-[#FAF6F0] text-[#2B2320] flex flex-col md:flex-row">
      
      {/* Desktop Sidebar Navigation */}
      <aside className={`hidden md:flex flex-col w-64 ${headerBg} text-white p-6 shadow-xl transition-colors duration-300 justify-between`}>
        <div className="space-y-6">
          <div className="flex items-center gap-3">
            <span className="text-2xl text-[#FFDF7D]">♡</span>
            <div>
              <h1 className="font-serif font-bold text-lg leading-tight">Mon Parcours</h1>
              <p className="text-xs text-[#F0DEC9]">Fatou ♥ · RMA East Side</p>
            </div>
          </div>
          <nav className="space-y-1">
            {navItems.map(item => (
              <button
                key={item.id}
                onClick={() => setCurrentTab(item.id)}
                className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold transition ${currentTab === item.id ? 'bg-white/20 text-white shadow-inner' : 'text-white/80 hover:bg-white/10 hover:text-white'}`}
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
            className="w-full py-2 text-xs font-bold bg-black/20 hover:bg-black/30 rounded-xl transition flex items-center justify-center gap-2"
          >
            {isGoldTheme ? '✨ Thème Or Actif' : '🤎 Thème Fatou Actif'}
          </button>
          <button 
            onClick={() => alert("Résumé PDF généré et téléchargé avec succès !")}
            className="w-full py-2 text-xs font-bold bg-white text-[#5C3B24] hover:bg-[#F4EBD8] rounded-xl transition flex items-center justify-center gap-2 shadow"
          >
            <Download size={14} /> Télécharger Résumé PDF
          </button>
        </div>
      </aside>

      {/* Main Container */}
      <div className="flex-1 flex flex-col min-h-screen">
        
        {/* Mobile Header */}
        <div className={`md:hidden ${headerBg} text-white p-4 flex items-center justify-between shadow-md transition-colors duration-300 sticky top-0 z-20`}>
          <div className="flex items-center gap-2">
            <span className="text-xl text-[#FFDF7D]">♡</span>
            <div>
              <h1 className="text-base font-serif font-bold leading-tight">Mon Parcours</h1>
              <p className="text-[10px] text-[#F0DEC9]">Fatou ♥ · RMA</p>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <button 
              onClick={() => setIsGoldTheme(!isGoldTheme)}
              className="px-2 py-1 text-[10px] font-extrabold bg-black/20 rounded-full border border-white/20"
            >
              {isGoldTheme ? '✨ Or' : '🤎 Fatou'}
            </button>
            <button 
              onClick={() => alert("Résumé PDF généré !")}
              className="px-2 py-1 text-[10px] font-bold bg-black/30 rounded-full border border-white/20 flex items-center gap-1"
            >
              <Download size={10} /> PDF
            </button>
          </div>
        </div>

        {/* Content Area */}
        <main className="flex-1 max-w-4xl w-full mx-auto p-4 sm:p-6 lg:p-8 space-y-6 pb-24 md:pb-8">
          
          {/* TAB 1: HOME */}
          {currentTab === 'home' && (
            <div className="space-y-6">
              {/* Hero Banner */}
              <div className="bg-gradient-to-br from-[#F4EBD8] via-[#FCF9F5] to-[#EAE0D0] border border-[#D8C7B3] rounded-2xl p-6 shadow-sm">
                <span className="text-xs font-extrabold text-[#5C3B24] tracking-wider">VOTRE PARCOURS FIV</span>
                <h2 className="text-2xl sm:text-3xl font-serif font-medium text-[#2B2320] mt-1">De la première injection au premier battement de cœur</h2>
                <p className="text-sm text-[#6E6058] mt-2">Chaque étape chez RMA of New York est documentée avec précision et suivie en toute sécurité.</p>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {/* Graduation Countdown Card */}
                <div className="bg-[#FBF6EE] border border-[#5C3B24] rounded-2xl p-5 shadow-sm space-y-3">
                  <div className="flex justify-between items-center">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-full bg-[#F4EBD8] flex items-center justify-center text-lg">🎓</div>
                      <div>
                        <h3 className="font-serif font-bold text-base text-[#5C3B24]">Compte à rebours de Graduation</h3>
                        <p className="text-xs text-[#6E6058]">Transition RMA vers OBGYN (Semaines 8–10)</p>
                      </div>
                    </div>
                    <span className="text-xs font-bold bg-[#EAF4ED] text-[#2B7853] px-2.5 py-1 rounded-full border border-[#C7E2D0]">Semaine 9</span>
                  </div>
                  <div className="bg-white rounded-xl p-4 border border-[#E5D4C3] flex justify-between items-center">
                    <div>
                      <p className="text-xs text-[#6E6058]">Âge gestationnel estimé :</p>
                      <p className="text-base font-serif font-bold text-[#2B2320]">9 semaines et 2 jours</p>
                    </div>
                    <div className="bg-[#FAF6F0] border border-[#E2D3C4] px-4 py-2 rounded-xl text-center">
                      <span className="text-xl font-extrabold text-[#5C3B24]">14</span>
                      <p className="text-[9px] font-bold text-[#6E6058]">JOURS CIBLE</p>
                    </div>
                  </div>
                </div>

                {/* Continued Support Meds */}
                <div className="bg-white border border-[#E2D3C4] rounded-2xl p-5 shadow-sm space-y-3">
                  <div className="flex justify-between items-center">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-full bg-[#EAF4ED] flex items-center justify-center text-lg">💊</div>
                      <div>
                        <h3 className="font-serif font-bold text-base text-[#2B2320]">Soutien Hormonal Continu</h3>
                        <p className="text-xs text-[#6E6058]">Progestérone (PIO / Ovules) & Œstrogènes</p>
                      </div>
                    </div>
                    <span className="text-xs font-bold bg-[#F6ECE0] text-[#5C3B24] px-2.5 py-1 rounded-full">Essentiel</span>
                  </div>
                  <div className="space-y-2.5">
                    <div className="bg-[#FAF7F3] border border-[#E8DFC9] p-3 rounded-xl flex items-center justify-between">
                      <div>
                        <p className="text-xs sm:text-sm font-bold text-[#2B2320]">Progestérone en Huile (PIO) 50 mg</p>
                        <p className="text-xs text-[#8D6648]">⏰ 08 h 00 · Injection intramusculaire</p>
                      </div>
                      <input type="checkbox" defaultChecked className="w-5 h-5 accent-[#2B7853] rounded cursor-pointer" />
                    </div>
                    <div className="bg-[#FAF7F3] border border-[#E8DFC9] p-3 rounded-xl flex items-center justify-between">
                      <div>
                        <p className="text-xs sm:text-sm font-bold text-[#2B2320]">Ovules de Progestérone 200 mg</p>
                        <p className="text-xs text-[#8D6648]">⏰ 08 h · 14 h · 22 h (3x / jour)</p>
                      </div>
                      <input type="checkbox" className="w-5 h-5 accent-[#2B7853] rounded cursor-pointer" />
                    </div>
                  </div>
                </div>
              </div>

              {/* Vos prochaines actions */}
              <div className="bg-white border border-[#E2D3C4] rounded-2xl p-5 shadow-sm space-y-3">
                <div className="flex justify-between items-center">
                  <h3 className="font-serif font-bold text-base text-[#5C3B24]">✓ Vos prochaines actions</h3>
                  <span className="text-[10px] font-extrabold bg-[#EBDCCF] text-[#543722] px-2.5 py-1 rounded-lg">OCTOBRE 2026</span>
                </div>
                <div className="space-y-2">
                  {checklist.slice(0, 3).map(item => (
                    <div key={item.id} onClick={() => toggleCheck(item.id)} className="flex items-center gap-3 cursor-pointer p-2 hover:bg-[#FAF6F0] rounded-xl transition">
                      <input type="checkbox" checked={item.isCompleted} onChange={() => {}} className="w-5 h-5 accent-[#2B7853] cursor-pointer" />
                      <div className="flex-1 min-w-0">
                        <p className={`text-sm font-semibold truncate ${item.isCompleted ? 'line-through text-[#6E6058]' : 'text-[#2B2320]'}`}>{item.title}</p>
                        <p className="text-xs text-[#6E6058]">{item.dateText}</p>
                      </div>
                    </div>
                  ))}
                </div>
                <button onClick={() => setCurrentTab('checklist')} className="w-full py-2.5 text-xs sm:text-sm font-bold text-[#5C3B24] border border-[#5C3B24] rounded-xl hover:bg-[#F4EBD8] transition">
                  Voir toute la liste de contrôle
                </button>
              </div>
            </div>
          )}

          {/* TAB 2: CALENDAR */}
          {currentTab === 'calendar' && (
            <div className="space-y-6">
              <h2 className="text-2xl font-serif font-bold text-[#5C3B24]">Calendrier Médical du Protocole</h2>
              <div className="bg-white border border-[#E2D3C4] rounded-2xl p-6 shadow-sm space-y-4">
                <div className="flex justify-between items-center">
                  <span className="font-serif font-bold text-base text-[#5C3B24]">Octobre 2026 (FET & Suivi)</span>
                  <span className="text-xs bg-[#EAF4ED] text-[#2B7853] px-3 py-1 rounded-full font-bold">10 Étapes clés</span>
                </div>
                <div className="grid grid-cols-7 gap-2 text-center text-xs font-bold text-[#6E6058]">
                  <span>Lun</span><span>Mar</span><span>Mer</span><span>Jeu</span><span>Ven</span><span>Sam</span><span>Dim</span>
                </div>
                <div className="grid grid-cols-7 gap-2 text-center text-sm">
                  {Array.from({ length: 31 }).map((_, i) => {
                    const day = i + 1;
                    const hasEvent = [1, 2, 6, 9, 11, 13, 26, 28, 30, 31].includes(day);
                    return (
                      <div key={day} className={`p-3 rounded-xl ${day === 28 ? 'bg-[#5C3B24] text-white font-bold shadow' : hasEvent ? 'bg-[#F7EEE4] font-bold text-[#5C3B24]' : 'text-[#2B2320]'}`}>
                        {day}
                      </div>
                    );
                  })}
                </div>
                <div className="bg-[#FCF9F5] border border-[#E2D3C4] p-4 rounded-xl space-y-2">
                  <span className="text-xs font-bold text-[#5C3B24] bg-[#F4EBD8] px-2.5 py-1 rounded">28 Octobre · 08 h 00</span>
                  <h4 className="font-serif font-bold text-base">Hystérosonographie SIS</h4>
                  <p className="text-sm text-[#6E6058]">RMA East Side (635 Madison Ave). Examen détaillé de la cavité utérine.</p>
                </div>
              </div>
            </div>
          )}

          {/* TAB 3: LAB */}
          {currentTab === 'lab' && (
            <div className="space-y-6">
              <h2 className="text-2xl font-serif font-bold text-[#5C3B24]">Dossier de Laboratoire (RMA NY)</h2>
              <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
                <div className="bg-white border border-[#E2D3C4] p-4 rounded-2xl shadow-sm text-center">
                  <span className="text-2xl">🥚</span>
                  <p className="text-3xl font-serif font-bold text-[#5C3B24] mt-2">42</p>
                  <p className="text-xs font-semibold text-[#6E6058] mt-1">Ovocytes prélevés</p>
                </div>
                <div className="bg-white border border-[#E2D3C4] p-4 rounded-2xl shadow-sm text-center">
                  <span className="text-2xl">◉</span>
                  <p className="text-3xl font-serif font-bold text-[#5C3B24] mt-2">34</p>
                  <p className="text-xs font-semibold text-[#6E6058] mt-1">Ovocytes matures</p>
                </div>
                <div className="bg-white border border-[#E2D3C4] p-4 rounded-2xl shadow-sm text-center">
                  <span className="text-2xl">✧</span>
                  <p className="text-3xl font-serif font-bold text-[#5C3B24] mt-2">11</p>
                  <p className="text-xs font-semibold text-[#6E6058] mt-1">Fécondés ICSI</p>
                </div>
                <div className="bg-white border border-[#E2D3C4] p-4 rounded-2xl shadow-sm text-center">
                  <span className="text-2xl">❄</span>
                  <p className="text-3xl font-serif font-bold text-[#5C3B24] mt-2">8</p>
                  <p className="text-xs font-semibold text-[#6E6058] mt-1">Blastocystes vitrifiés</p>
                </div>
              </div>
            </div>
          )}

          {/* TAB 4: CHECKLIST */}
          {currentTab === 'checklist' && (
            <div className="space-y-6">
              <h2 className="text-2xl font-serif font-bold text-[#5C3B24]">Liste de Contrôle Complète</h2>
              <div className="space-y-3">
                {checklist.map(item => (
                  <div key={item.id} onClick={() => toggleCheck(item.id)} className="bg-white border border-[#E2D3C4] p-4 rounded-2xl shadow-sm flex items-start gap-4 cursor-pointer hover:bg-[#FAF6F0] transition">
                    <input type="checkbox" checked={item.isCompleted} onChange={() => {}} className="w-6 h-6 accent-[#2B7853] mt-1 cursor-pointer" />
                    <div className="flex-1">
                      <div className="flex justify-between items-center mb-1">
                        <span className="text-xs font-bold text-[#5C3B24]">{item.dateText}</span>
                        <span className="text-xs font-bold px-3 py-0.5 rounded-full bg-[#EAF4ED] text-[#2B7853]">{item.badgeText}</span>
                      </div>
                      <h4 className={`text-base font-semibold ${item.isCompleted ? 'line-through text-[#6E6058]' : 'text-[#2B2320]'}`}>{item.title}</h4>
                      <p className="text-sm text-[#6E6058] mt-1">{item.description}</p>
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
              <form onSubmit={addNote} className="bg-white border border-[#E2D3C4] p-4 rounded-2xl shadow-sm space-y-3">
                <input 
                  type="text" 
                  placeholder="Titre de la note..." 
                  value={newNoteTitle} 
                  onChange={e => setNewNoteTitle(e.target.value)} 
                  className="w-full text-sm p-3 border border-[#E2D3C4] rounded-xl focus:outline-none focus:border-[#5C3B24]"
                />
                <textarea 
                  placeholder="Écrivez vos pensées et observations ici..." 
                  value={newNoteContent} 
                  onChange={e => setNewNoteContent(e.target.value)} 
                  className="w-full text-sm p-3 border border-[#E2D3C4] rounded-xl focus:outline-none focus:border-[#5C3B24] h-28"
                />
                <button type="submit" className="w-full py-2.5 bg-[#5C3B24] text-white text-sm font-bold rounded-xl hover:bg-[#452B18] transition shadow">
                  Ajouter au Journal
                </button>
              </form>
              <div className="space-y-4">
                {notes.map(note => (
                  <div key={note.id} className="bg-white border border-[#E2D3C4] p-4 rounded-2xl shadow-sm space-y-2 relative">
                    <button onClick={() => deleteNote(note.id)} className="absolute top-4 right-4 text-red-500 hover:text-red-700 p-1">
                      <Trash2 size={16} />
                    </button>
                    <span className="text-xs font-bold text-[#5C3B24] bg-[#F4EBD8] px-2.5 py-1 rounded-md">{note.date}</span>
                    <h4 className="text-base font-serif font-bold text-[#2B2320]">{note.title}</h4>
                    <p className="text-sm text-[#6E6058]">{note.content}</p>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* TAB 6: GROSSESSE / OBSTETRIC JOURNEY */}
          {currentTab === 'obstetric' && (
            <div className="space-y-6">
              <h2 className="text-2xl font-serif font-bold text-[#5C3B24]">Parcours Grossesse (RMA ➔ Naissance)</h2>
              <div className="bg-[#FBF6EE] border border-[#5C3B24] p-5 rounded-2xl text-sm text-[#6E6058] space-y-2">
                <p className="font-bold text-base text-[#5C3B24]">Suivi obstétrical détaillé de la graduation à juin 2027 :</p>
                <p>• <strong>Semaines 8–10 (Déc. 2026) :</strong> Graduation RMA & transition vers l'obstétricien (OBGYN).</p>
                <p>• <strong>Semaines 11–13 (Janv. 2027) :</strong> Échographie NT Scan (Clarté nucale) & dépistage prénatal.</p>
                <p>• <strong>Semaines 20–22 (Fév. – Mars 2027) :</strong> Grande Échographie Morphologique (Anatomy Scan).</p>
                <p>• <strong>Semaines 34–36 (Mai 2027) :</strong> Dépistage GBS & préparation au plan de naissance.</p>
                <p>• <strong>Juin 2027 :</strong> Naissance de bébé ! 🎉</p>
              </div>
            </div>
          )}

        </main>

        {/* Mobile Bottom Navigation Bar */}
        <div className="md:hidden fixed bottom-0 left-0 right-0 bg-[#FAF6F0] border-t border-[#E2D3C4] px-2 py-2 flex justify-around items-center z-30 shadow-lg">
          {navItems.map(tab => (
            <button
              key={tab.id}
              onClick={() => setCurrentTab(tab.id)}
              className={`flex flex-col items-center py-1.5 px-3 rounded-xl transition ${currentTab === tab.id ? 'text-[#5C3B24] font-bold bg-[#F4EBD8]' : 'text-[#6E6058] hover:text-[#2B2320]'}`}
            >
              <span className="text-lg">{tab.icon}</span>
              <span className="text-[9px] mt-0.5">{tab.label}</span>
            </button>
          ))}
        </div>

      </div>
    </div>
  );
}
