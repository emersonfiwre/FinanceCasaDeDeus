package br.com.casadedeus.service.listener

data class AuthenticationListener(var isFingerprint: Boolean = false, val isLogged: Boolean, val isProtected: Boolean)