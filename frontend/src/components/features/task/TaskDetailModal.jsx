import React, {useEffect, useMemo, useState} from 'react';
import {X, Edit2, Check, User, Calendar, Trash2, ChevronDown, Clock, AlertTriangle} from 'lucide-react';
import useDetailTask from '/src/hooks/task/useDetailTask.jsx';
import useUpdateFieldTask from '/src/hooks/task/useUpdateFieldTask.jsx';
import useDeleteTask from '/src/hooks/task/useDeleteTask.jsx';
import {colorMap} from '/src/data/project/constants.jsx';
import {taskPriorityOptions, taskStatusOptions, dayLabelOptions} from "/src/data/task/constants.jsx";

const TaskDetailModal = ({taskId, isOpen, onClose, onUpdate}) => {
  const {data: task, loading, error, refetch} = useDetailTask(taskId);
  const {updateFieldTask, loading: updateFieldLoading} = useUpdateFieldTask();
  const {deleteTask, loading: deleteLoading} = useDeleteTask();

  const detail = useMemo(() => task ?? null, [task]);

  // 인라인 편집 상태
  const [editTitle, setEditTitle] = useState(false);
  const [editDesc, setEditDesc] = useState(false);
  const [editPriority, setEditPriority] = useState(false);
  const [editStatus, setEditStatus] = useState(false);
  const [editDayLabel, setEditDayLabel] = useState(false);
  const [editSchedule, setEditSchedule] = useState(false);

  // 편집 값
  const [form, setForm] = useState({
    title: '',
    description: '',
    priority: 'MEDIUM',
    status: 'TODO',
    dayLabel: 'MORNING',
    colorId: 1,
    projectId: null,
    schedule: {
      startDate: '',
      startTime: '',
      startTimeEnabled: false,
      dueDate: '',
      dueTime: '',
      dueTimeEnabled: false,
    }
  });

  // detail 변경 시 폼 초기화
  useEffect(() => {
    if (!detail) return;
    setForm({
      title: detail.title ?? '',
      description: detail.description ?? '',
      priority: detail.priority ?? 'MEDIUM',
      status: detail.status ?? 'TODO',
      dayLabel: detail.dayLabel ?? 'MORNING',
      colorId: Number(detail.colorId ?? 1),
      projectId: detail.projectId ?? null,
      schedule: {
        startDate: detail.schedule?.startDate ?? '',
        startTime: detail.schedule?.startTime ?? '',
        startTimeEnabled: detail.schedule?.startTimeEnabled ?? false,
        dueDate: detail.schedule?.dueDate ?? '',
        dueTime: detail.schedule?.dueTime ?? '',
        dueTimeEnabled: detail.schedule?.dueTimeEnabled ?? false,
      }
    });
  }, [detail]);

  // 모달 열릴 때 배경 스크롤 잠금
  useEffect(() => {
    if (isOpen) {
      const prev = document.body.style.overflow;
      document.body.style.overflow = 'hidden';
      return () => {
        document.body.style.overflow = prev;
      };
    }
  }, [isOpen]);

  // 모달 닫힐 때 인라인 편집 상태 리셋
  const resetInlineEdits = () => {
    setEditTitle(false);
    setEditDesc(false);
    setEditPriority(false);
    setEditStatus(false);
    setEditDayLabel(false);
    setEditSchedule(false);
  };

  useEffect(() => {
    if (!isOpen) {
      resetInlineEdits();
    }
  }, [isOpen]);

  if (!isOpen) return null;

  const headerColor = colorMap[detail?.colorId] ?? 'bg-gradient-to-br from-slate-500 to-slate-700';

  // 우선순위 색상
  const getPriorityColor = (priority) => {
    switch (priority) {
      case 'LOW': return 'text-green-500';
      case 'MEDIUM': return 'text-yellow-500';
      case 'HIGH': return 'text-orange-500';
      case 'URGENT': return 'text-red-500';
      default: return 'text-gray-500';
    }
  };

  const saveField = async (fieldType, value) => {
    try {
      const res = await updateFieldTask(taskId, {
        fieldType,
        value,
      });
      if (res?.success) {
        await refetch();
        onUpdate && onUpdate();
        return true;
      }
      return false;
    } catch (err) {
      console.error(`Failed to update field ${fieldType}:`, err);
      return false;
    }
  };

  const handleBackdropClick = (e) => {
    if (e.target === e.currentTarget) {
      resetInlineEdits();
      onClose();
    }
  };

  const handleCloseClick = () => {
    resetInlineEdits();
    onClose();
  };

  const handleDelete = async () => {
    if (window.confirm('정말로 이 작업을 삭제하시겠습니까?')) {
      const res = await deleteTask(taskId);
      if (res?.success) {
        resetInlineEdits();
        onClose();
        onUpdate && onUpdate();
      }
    }
  };

  // DateTimeRow Component
  const DateTimeRow = ({label, date, onDate, time, onTime, timeEnabled, onToggleTime}) => {
    return (
      <div className="space-y-2">
        <div className="flex items-center justify-between">
          <span className="text-sm font-medium text-gray-700">{label}</span>
          <div className="flex items-center space-x-2">
            <span className="text-xs text-gray-500">시간 사용</span>
            {/* Toggle Switch */}
            <button
              onClick={() => onToggleTime(!timeEnabled)}
              className={`relative inline-flex h-5 w-9 items-center rounded-full transition-colors ${
                timeEnabled ? 'bg-blue-600' : 'bg-gray-300'
              }`}
            >
              <span
                className={`inline-block h-3 w-3 transform rounded-full bg-white transition-transform ${
                  timeEnabled ? 'translate-x-5' : 'translate-x-1'
                }`}
              />
            </button>
          </div>
        </div>
        <div className="flex space-x-2">
          <input
            type="date"
            value={date}
            onChange={(e) => onDate(e.target.value)}
            className="flex-1 px-3 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
          <input
            type="time"
            value={time}
            onChange={(e) => onTime(e.target.value)}
            disabled={!timeEnabled}
            className="w-32 px-3 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:bg-gray-100 disabled:text-gray-400"
          />
        </div>
      </div>
    );
  };

  // Custom Dropdown Component
  const CustomDropdown = ({value, options, onChange, placeholder, className = ""}) => {
    const [isOpen, setIsOpen] = useState(false);
    const selectedOption = options.find(opt => opt.value === value);

    return (
      <div className={`relative ${className}`}>
        <button
          className="w-full bg-white/20 backdrop-blur-sm border border-white/30 rounded-lg px-3 py-2 text-left text-white text-sm font-medium flex items-center justify-between hover:bg-white/30 transition-all"
          onClick={() => setIsOpen(!isOpen)}
        >
          <span>{selectedOption?.label || placeholder}</span>
          <ChevronDown className={`w-4 h-4 transition-transform ${isOpen ? 'rotate-180' : ''}`}/>
        </button>

        {isOpen && (
          <div className="absolute top-full left-0 right-0 mt-1 bg-white rounded-lg shadow-lg border border-gray-200 z-10 max-h-40 overflow-y-auto">
            {options.map((option) => (
              <button
                key={option.value}
                className="w-full px-3 py-2 text-left text-gray-700 hover:bg-gray-50 first:rounded-t-lg last:rounded-b-lg transition-colors"
                onClick={() => {
                  onChange(option.value);
                  setIsOpen(false);
                }}
              >
                {option.label}
              </button>
            ))}
          </div>
        )}
      </div>
    );
  };

  if (loading) {
    return (
      <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
        <div className="bg-white rounded-2xl p-6 max-w-md w-full mx-4">
          <div className="flex items-center justify-center py-8">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"/>
            <span className="ml-2 text-gray-600">작업 정보를 불러오는 중...</span>
          </div>
        </div>
      </div>
    );
  }

  if (error || !detail) {
    return (
      <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
        <div className="bg-white rounded-2xl p-6 max-w-md w-full mx-4">
          <div className="text-center py-8">
            <p className="text-red-600 mb-4">작업 정보를 불러올 수 없습니다.</p>
            <button onClick={handleCloseClick}
                    className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600">
              닫기
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50" onClick={handleBackdropClick}>
      <div className="bg-white rounded-2xl max-w-md w-full mx-4 max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-gray-200">
          <h2 className="text-xl font-bold text-gray-800">작업 상세</h2>
          <button onClick={handleCloseClick}
                  className="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100">
            <X className="w-5 h-5 text-gray-500"/>
          </button>
        </div>

        {/* Content */}
        <div className="p-6 space-y-6">
          {/* Task Header */}
          <div className={`${headerColor} rounded-2xl p-6 text-white relative`}>
            <div className="flex items-center justify-between mb-3">
              <div className="w-12 h-12 bg-white/20 rounded-full flex items-center justify-center">
                <User className="w-6 h-6"/>
              </div>

              {/* Priority 인라인 편집 */}
              <div className="flex items-center space-x-2">
                {!editPriority ? (
                  <>
                    <div className="flex items-center space-x-1">
                      <AlertTriangle className={`w-4 h-4 ${getPriorityColor(form.priority)}`}/>
                      <span className="text-sm opacity-90 font-medium">
                        {taskPriorityOptions.find(opt => opt.value === form.priority)?.label || form.priority}
                      </span>
                    </div>
                    <button
                      className="w-8 h-8 flex items-center justify-center rounded-full hover:bg-white/10"
                      onClick={() => setEditPriority(true)}
                      title="우선순위 편집"
                    >
                      <Edit2 className="w-4 h-4"/>
                    </button>
                  </>
                ) : (
                  <>
                    <CustomDropdown
                      value={form.priority}
                      options={taskPriorityOptions}
                      onChange={(v) => setForm((s) => ({...s, priority: v}))}
                      placeholder="우선순위"
                      className="min-w-[100px]"
                    />
                    <button
                      className="w-8 h-8 flex items-center justify-center rounded-full hover:bg-white/10"
                      disabled={updateFieldLoading}
                      onClick={async () => {
                        const ok = await saveField('priority', form.priority);
                        if (ok) setEditPriority(false);
                      }}
                      title="우선순위 저장"
                    >
                      <Check className="w-4 h-4"/>
                    </button>
                  </>
                )}
              </div>
            </div>

            {/* Title 인라인 편집 */}
            <div className="flex items-start justify-between mb-4">
              {!editTitle ? (
                <>
                  <h3 className="text-xl font-bold break-words">{form.title}</h3>
                  <button
                    className="ml-3 w-8 h-8 flex items-center justify-center rounded-full hover:bg-white/10"
                    onClick={() => setEditTitle(true)}
                    title="제목 편집"
                  >
                    <Edit2 className="w-4 h-4"/>
                  </button>
                </>
              ) : (
                <div className="flex-1 flex items-center space-x-2">
                  <input
                    value={form.title}
                    onChange={(e) => setForm((s) => ({...s, title: e.target.value}))}
                    placeholder="작업 제목"
                    className="w-full bg-transparent border-none outline-none text-xl font-bold text-white placeholder-white/60"
                  />
                  <button
                    className="w-8 h-8 flex items-center justify-center rounded-full hover:bg-white/10"
                    disabled={updateFieldLoading}
                    onClick={async () => {
                      const ok = await saveField('title', form.title);
                      if (ok) setEditTitle(false);
                    }}
                    title="제목 저장"
                  >
                    <Check className="w-4 h-4"/>
                  </button>
                </div>
              )}
            </div>

            {/* Status 인라인 편집 */}
            <div className="flex items-center justify-between mb-3">
              <span className="text-sm opacity-90">상태</span>
              <div className="flex items-center space-x-2">
                {!editStatus ? (
                  <>
                    <span className="text-sm font-medium">
                      {taskStatusOptions.find(opt => opt.value === form.status)?.label || form.status}
                    </span>
                    <button
                      className="w-6 h-6 flex items-center justify-center rounded-full hover:bg-white/10"
                      onClick={() => setEditStatus(true)}
                      title="상태 편집"
                    >
                      <Edit2 className="w-3 h-3"/>
                    </button>
                  </>
                ) : (
                  <>
                    <CustomDropdown
                      value={form.status}
                      options={taskStatusOptions}
                      onChange={(v) => setForm((s) => ({...s, status: v}))}
                      placeholder="상태 선택"
                      className="min-w-[100px]"
                    />
                    <button
                      className="w-6 h-6 flex items-center justify-center rounded-full hover:bg-white/10"
                      disabled={updateFieldLoading}
                      onClick={async () => {
                        const ok = await saveField('status', form.status);
                        if (ok) setEditStatus(false);
                      }}
                      title="상태 저장"
                    >
                      <Check className="w-3 h-3"/>
                    </button>
                  </>
                )}
              </div>
            </div>

            {/* Day Label 인라인 편집 */}
            <div className="flex items-center justify-between">
              <span className="text-sm opacity-90">시간대</span>
              <div className="flex items-center space-x-2">
                {!editDayLabel ? (
                  <>
                    <span className="text-sm font-medium">
                      {dayLabelOptions.find(opt => opt.value === form.dayLabel)?.label || form.dayLabel}
                    </span>
                    <button
                      className="w-6 h-6 flex items-center justify-center rounded-full hover:bg-white/10"
                      onClick={() => setEditDayLabel(true)}
                      title="시간대 편집"
                    >
                      <Edit2 className="w-3 h-3"/>
                    </button>
                  </>
                ) : (
                  <>
                    <CustomDropdown
                      value={form.dayLabel}
                      options={dayLabelOptions}
                      onChange={(v) => setForm((s) => ({...s, dayLabel: v}))}
                      placeholder="시간대 선택"
                      className="min-w-[80px]"
                    />
                    <button
                      className="w-6 h-6 flex items-center justify-center rounded-full hover:bg-white/10"
                      disabled={updateFieldLoading}
                      onClick={async () => {
                        const ok = await saveField('dayLabel', form.dayLabel);
                        if (ok) setEditDayLabel(false);
                      }}
                      title="시간대 저장"
                    >
                      <Check className="w-3 h-3"/>
                    </button>
                  </>
                )}
              </div>
            </div>
          </div>

          {/* Description 인라인 편집 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">설명</label>
            {!editDesc ? (
              <div className="flex items-start justify-between">
                <p className="text-gray-600 whitespace-pre-wrap break-words flex-1">
                  {form.description || '설명이 없습니다.'}
                </p>
                <button
                  className="ml-2 w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100"
                  onClick={() => setEditDesc(true)}
                  title="설명 편집"
                >
                  <Edit2 className="w-4 h-4 text-gray-600"/>
                </button>
              </div>
            ) : (
              <div className="flex items-start space-x-2">
                <textarea
                  className="w-full bg-transparent border-none outline-none text-gray-600 px-0 py-0 resize-none"
                  rows={3}
                  value={form.description}
                  onChange={(e) => setForm((s) => ({...s, description: e.target.value}))}
                  placeholder="작업 설명"
                />
                <button
                  className="w-10 h-10 flex items-center justify-center rounded-full hover:bg-gray-100"
                  disabled={updateFieldLoading}
                  onClick={async () => {
                    const ok = await saveField('description', form.description || null);
                    if (ok) setEditDesc(false);
                  }}
                  title="설명 저장"
                >
                  <Check className="w-5 h-5 text-gray-700"/>
                </button>
              </div>
            )}
          </div>

          {/* Schedule 인라인 편집 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              <Calendar className="w-4 h-4 inline mr-2" />
              일정
            </label>

            {!editSchedule ? (
              <div className="flex items-center justify-between">
                <p className="text-gray-600">{
                  (() => {
                    const parts = [];
                    if (form.schedule.startDate) {
                      parts.push(`시작: ${form.schedule.startDate}${form.schedule.startTimeEnabled && form.schedule.startTime ? ` ${form.schedule.startTime}` : ''}`);
                    }
                    if (form.schedule.dueDate) {
                      parts.push(`마감: ${form.schedule.dueDate}${form.schedule.dueTimeEnabled && form.schedule.dueTime ? ` ${form.schedule.dueTime}` : ''}`);
                    }
                    return parts.length > 0 ? parts.join(' | ') : '일정이 없습니다.';
                  })()
                }</p>
                <button
                  className="ml-2 w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100"
                  onClick={() => setEditSchedule(true)}
                  title="일정 편집"
                >
                  <Edit2 className="w-4 h-4 text-gray-600" />
                </button>
              </div>
            ) : (
              <div className="space-y-4">
                <DateTimeRow
                  label="시작"
                  date={form.schedule.startDate}
                  onDate={(v) =>
                    setForm((s) => ({
                      ...s,
                      schedule: { ...s.schedule, startDate: v }
                    }))
                  }
                  time={form.schedule.startTime}
                  onTime={(v) =>
                    setForm((s) => ({
                      ...s,
                      schedule: { ...s.schedule, startTime: v }
                    }))
                  }
                  timeEnabled={form.schedule.startTimeEnabled}
                  onToggleTime={(v) =>
                    setForm((s) => ({
                      ...s,
                      schedule: { ...s.schedule, startTimeEnabled: v }
                    }))
                  }
                />
                <DateTimeRow
                  label="마감"
                  date={form.schedule.dueDate}
                  onDate={(v) =>
                    setForm((s) => ({
                      ...s,
                      schedule: { ...s.schedule, dueDate: v }
                    }))
                  }
                  time={form.schedule.dueTime}
                  onTime={(v) =>
                    setForm((s) => ({
                      ...s,
                      schedule: { ...s.schedule, dueTime: v }
                    }))
                  }
                  timeEnabled={form.schedule.dueTimeEnabled}
                  onToggleTime={(v) =>
                    setForm((s) => ({
                      ...s,
                      schedule: { ...s.schedule, dueTimeEnabled: v }
                    }))
                  }
                />
                <div className="flex items-center justify-end">
                  <button
                    className="w-10 h-10 flex items-center justify-center rounded-full hover:bg-gray-100"
                    disabled={updateFieldLoading}
                    onClick={async () => {
                      const ok = await saveField("schedule", {
                        startDate: form.schedule.startDate,
                        startTime: form.schedule.startTime,
                        startTimeEnabled: form.schedule.startTimeEnabled,
                        dueDate: form.schedule.dueDate,
                        dueTime: form.schedule.dueTime,
                        dueTimeEnabled: form.schedule.dueTimeEnabled
                      });
                      if (ok) setEditSchedule(false);
                    }}
                    title="일정 저장"
                  >
                    <Check className="w-5 h-5 text-gray-700" />
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Footer */}
        <div className="p-6 border-t border-gray-200">
          <div className="flex space-x-3">
            <button
              onClick={() => {
                resetInlineEdits();
                onClose();
              }}
              disabled={updateFieldLoading}
              className="flex-1 flex items-center justify-center py-3 bg-green-600 text-white rounded-xl font-medium hover:bg-green-700 disabled:bg-gray-400 transition-colors"
            >
              <Check className="w-4 h-4 mr-2"/>
              {updateFieldLoading ? '저장 중...' : '완료'}
            </button>

            <button
              onClick={handleDelete}
              disabled={deleteLoading}
              className="flex-1 flex items-center justify-center py-3 bg-red-600 text-white rounded-xl font-medium hover:bg-red-700 disabled:bg-gray-400 transition-colors"
            >
              <Trash2 className="w-4 h-4 mr-2"/>
              {deleteLoading ? '삭제 중...' : '삭제'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TaskDetailModal;