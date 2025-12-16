/**
 * UserId 自动修复脚本
 * 
 * 使用方法：
 * 1. 登录系统后，在浏览器控制台粘贴此脚本
 * 2. 按回车执行
 * 3. 脚本会自动检测并修复 userId 问题
 */

(async function fixUserId() {
  console.log('🔧 开始修复 userId 问题...\n');
  
  const API_BASE = 'http://localhost:8888';
  const token = localStorage.getItem('authorization');
  
  // 1. 检查 token
  if (!token) {
    console.error('❌ 错误：未找到 authorization token');
    console.log('💡 解决方案：请先登录系统');
    return;
  }
  console.log('✓ Token 存在');
  
  // 2. 检查当前 userId
  const currentUserId = localStorage.getItem('userId');
  console.log(`当前 userId: ${currentUserId || '❌ 不存在'}`);
  
  // 3. 尝试从 checkToken 获取
  console.log('\n正在从 checkToken 接口获取用户信息...');
  try {
    const checkTokenResp = await fetch(`${API_BASE}/common/checkToken`, {
      headers: { 'authorization': token }
    });
    const checkTokenData = await checkTokenResp.json();
    
    console.log('checkToken 响应:', checkTokenData);
    
    if (checkTokenData.code === 200 && checkTokenData.data) {
      // 尝试多种字段名
      const userId = checkTokenData.data.userId || 
                     checkTokenData.data.id || 
                     checkTokenData.data.user_id ||
                     checkTokenData.data.uid;
      
      if (userId) {
        console.log(`✓ 从 checkToken 找到 userId: ${userId}`);
        localStorage.setItem('userId', userId);
        console.log('✓ 已保存到 localStorage');
        console.log('\n✅ 修复完成！现在可以尝试人脸注册了');
        return;
      } else {
        console.warn('⚠️ checkToken 响应中没有找到 userId 字段');
        console.log('可用字段:', Object.keys(checkTokenData.data));
      }
    }
  } catch (error) {
    console.error('❌ checkToken 请求失败:', error);
  }
  
  // 4. 尝试从 getCurrentUser 获取
  console.log('\n正在从 getCurrentUser 接口获取用户信息...');
  try {
    const getCurrentUserResp = await fetch(`${API_BASE}/common/getCurrentUser`, {
      headers: { 'authorization': token }
    });
    const getCurrentUserData = await getCurrentUserResp.json();
    
    console.log('getCurrentUser 响应:', getCurrentUserData);
    
    if (getCurrentUserData.code === 200 && getCurrentUserData.data) {
      // 尝试多种字段名
      const userId = getCurrentUserData.data.userId || 
                     getCurrentUserData.data.id || 
                     getCurrentUserData.data.user_id ||
                     getCurrentUserData.data.uid;
      
      if (userId) {
        console.log(`✓ 从 getCurrentUser 找到 userId: ${userId}`);
        localStorage.setItem('userId', userId);
        console.log('✓ 已保存到 localStorage');
        console.log('\n✅ 修复完成！现在可以尝试人脸注册了');
        return;
      } else {
        console.warn('⚠️ getCurrentUser 响应中没有找到 userId 字段');
        console.log('可用字段:', Object.keys(getCurrentUserData.data));
      }
    }
  } catch (error) {
    console.error('❌ getCurrentUser 请求失败:', error);
  }
  
  // 5. 如果都失败了
  console.log('\n❌ 自动修复失败');
  console.log('\n📋 请手动执行以下步骤：');
  console.log('1. 查看上面的响应数据，找到用户ID字段');
  console.log('2. 手动设置：localStorage.setItem("userId", "你的用户ID")');
  console.log('3. 或者联系后端开发人员确认用户ID字段名');
})();
