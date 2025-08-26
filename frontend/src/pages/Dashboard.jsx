import React, {useCallback, useEffect, useMemo, useState} from 'react';
import SearchBar from "/src/components/ui/SearchBar.jsx";
import TabSelector from "/src/components/layout/TabSelector.jsx";
import HorizontalProjectScroll from "/src/components/ui/HorizontalProjectScroll.jsx";
import RecentActivityItem from "/src/components/features/RecentActivityItem.jsx";
import Header from "/src/components/layout/Header.jsx";
import BottomNav from "/src/components/layout/BottomNav.jsx";
import useSummaryProject from "/src/hooks/project/useSummaryProject.jsx";
import {mockRecentActivity} from "/src/data/mockProjects.jsx";
import HorizontalTaskScroll from "/src/components/ui/HorizontalTaskScroll.jsx";
import useSummaryTask from "/src/hooks/task/useSummaryTask.jsx";
import useTodayTask from "/src/hooks/task/useTodayTask.jsx";
import useSearchProjects from "/src/hooks/project/useSearchProjects.jsx";
import {ROUTES} from "/src/router/routes.js";
import {useNavigate} from "react-router-dom";

const colorMap = {
  1: 'bg-gradient-to-br from-blue-600 to-purple-700',
  2: 'bg-gradient-to-br from-blue-500 to-cyan-500',
  3: 'bg-gradient-to-br from-purple-600 to-pink-600',
  4: 'bg-gradient-to-br from-green-500 to-teal-600',
  5: 'bg-gradient-to-br from-orange-500 to-amber-600',
  6: 'bg-gradient-to-br from-pink-500 to-rose-600',
};

const Dashboard = () => {
  const [activeTab, setActiveTab] = useState('today');
  const [activeNavTab, setActiveNavTab] = useState('home');
  const {
    data: projectData,
    loading: projectLoading,
    error: projectError,
    refetch: refetchProjects
  } = useSummaryProject();

  const {
    data: taskData,
    loading: taskLoading,
    error: taskError,
    refetch: refetchTasks
  } = useSummaryTask();

  const {
    data: todayData,
    loading: todayLoading,
    error: todayError,
    refetch: refetchTodayTasks
  } = useTodayTask();

  const {
    keyword: searchKeyword,
    setKeyword: setSearchKeyword,
    data: searchedProjects,
    loading: searchLoading,
    error: searchError,
    clear: clearSearch,
  } = useSearchProjects("");

  const refetchAllTasks = useCallback(() => {
    refetchTasks()
    refetchTodayTasks()
  }, [refetchTasks, refetchTodayTasks]);

  const handleSearch = (query) => {
    setSearchKeyword(query);
    if (query && query.trim().length > 0) {
      setActiveTab('projects');
    }
  };

  const projectsForCard = useMemo(() => {
    const list = Array.isArray(projectData) ? projectData : [];

    return list.map((p) => ({
      id: p.id,
      title: p.name,
      type: p.visibility,
      date: `${p.startDate} ~ ${p.endDate}`,
      color: colorMap[p.colorId] ?? 'bg-gradient-to-br from-slate-500 to-slate-700',
      progress:
        typeof p.progress === 'number'
          ? (p.progress <= 1 ? Math.round(p.progress * 100) : Math.round(p.progress))
          : 0,
    }));
  }, [projectData]);

  const tasksForCard = useMemo(() => {
    const list = Array.isArray(taskData) ? taskData : [];

    return list.map((t) => ({
      id: t.id,
      title: t.title,
      status: t.status,
      priority: t.priority,
      dueDate: t.dueDate,
      dayLabel: t.dayLabel,
      color: colorMap[t.colorId] ?? 'bg-gradient-to-br from-slate-500 to-slate-700',
    }));
  }, [taskData]);

  const todayForCard = useMemo(() => {
    const list = Array.isArray(todayData) ? todayData : [];

    return list.map((t) => ({
      id: t.id,
      title: t.title,
      status: t.status,
      priority: t.priority,
      dueDate: t.dueDate,
      dayLabel: t.dayLabel,
      color: colorMap[t.colorId] ?? 'bg-gradient-to-br from-slate-500 to-slate-700',
    }));
  }, [todayData]);

  const searchedProjectsForCard = useMemo(() => {
    const list = Array.isArray(searchedProjects) ? searchedProjects : [];
    return list.map((p) => ({
      id: p.id,
      title: p.name ?? p.title ?? "(이름 없음)",
      type: p.visibility ?? "", // 없으면 빈 값
      date: p.startDate && p.endDate ? `${p.startDate} ~ ${p.endDate}` : "",
      color: colorMap[p.colorId] ?? 'bg-gradient-to-br from-slate-500 to-slate-700',
      progress:
        typeof p.progress === 'number'
          ? (p.progress <= 1 ? Math.round(p.progress * 100) : Math.round(p.progress))
          : 0,
    }));
  }, [searchedProjects]);

  const renderMainContent = () => {
    if (activeTab === 'today') {
      return (
        <>
          {todayLoading && <div className="py-6 text-gray-500">오늘 할 일 불러오는 중…</div>}
          {todayError && <div className="py-6 text-red-500">😅오늘 할 일을 불러오지 못했어요...</div>}
          {/*{!taskLoading && !taskError && (*/}
            <HorizontalTaskScroll
              tasks={todayForCard}
              title="Active Tasks"
              onUpdate={refetchAllTasks}
            />
          {/*)}*/}
        </>
      );
    }

    if (activeTab === 'projects') {
      const isSearching = (searchKeyword ?? "").trim().length > 0;

      if (isSearching) {
        return (
          <>
            {searchLoading && <div className="py-6 text-gray-500">검색 중…</div>}
            {searchError && <div className="py-6 text-red-500">😅검색 중 오류가 발생했어요: {searchError}</div>}
            {/*{!searchLoading && !searchError && (*/}
              <HorizontalProjectScroll
                projects={searchedProjectsForCard}
                title={`Search Results (${searchedProjectsForCard.length})`}
                onUpdate={refetchProjects} // 선택: 카드에서 수정 시 목록 갱신
              />
            {/*)}*/}
          </>
        );
      }

      // 기본(검색어 없음) → 요약 프로젝트
      return (
        <>
          {projectLoading && <div className="py-6 text-gray-500">프로젝트 불러오는 중…</div>}
          {projectError && <div className="py-6 text-red-500">😅프로젝트를 불러오지 못했어요...</div>}
          {/*{!projectLoading && !projectError && (*/}
            <HorizontalProjectScroll
              projects={projectsForCard}
              title="ALL Projects"
              onUpdate={refetchProjects}
            />
          {/*)}*/}
        </>
      );
    }

    if (activeTab === 'tasks') {
      return (
        <>
          {taskLoading && <div className="py-6 text-gray-500">태스크 불러오는 중…</div>}
          {taskError && <div className="py-6 text-red-500">😅할 일을 불러오지 못했어요...</div>}
          {/*{!taskLoading && !taskError && (*/}
            <HorizontalTaskScroll
              tasks={tasksForCard}
              title="ALL Tasks"
              onUpdate={refetchAllTasks}
            />
          {/*)}*/}
        </>
      );
    }
  }

  const navigate = useNavigate();

  const handleBottomAddTask = () => {
    navigate(ROUTES.TASK.CREATE);
  };

  const handleBottomTabChange = (id) => {
    // 네비게이션 탭 → 화면 탭 연동 (원하는 매핑으로 조정 가능)
    if (id === 'home') setActiveTab('today');
    else if (id === 'search') setActiveTab('projects');
    else if (id === 'calendar') setActiveTab('tasks');
    // 'notifications' 등은 추후 구현
  };

  return (
    <div>
      <Header/>
      {/* 메인 콘텐츠 */}
      <main className="px-6 pb-24">
        <SearchBar placeholder="Search" onSearch={handleSearch}/>

        <TabSelector activeTab={activeTab} onTabChange={(tab) => {
          setActiveTab(tab);
          if (tab !== 'projects') clearSearch();
        }}/>

        {/* 탭에 따른 동적 콘텐츠 */}
        {renderMainContent()}

        {/* 최근 활동 섹션 */}
        <section>
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-xl font-bold text-gray-800">지난주 회고</h2>
            <button className="text-blue-600 text-sm font-medium hover:text-blue-700">
              View All
            </button>
          </div>
          <div className="space-y-3">
            {mockRecentActivity.map((activity) => (
              <RecentActivityItem key={activity.id} activity={activity}/>
            ))}
          </div>
        </section>
      </main>
      <BottomNav
      activeTab={activeTab === 'today' ? 'home' : activeTab}
      onTabChange={handleBottomTabChange}
      onAddTask={handleBottomAddTask}
      />
    </div>
  );
};

export default Dashboard;